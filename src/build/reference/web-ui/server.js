var moment = require('moment');
var http = require('http');
var fileSystem = require('fs');
var Router = require('node-router');
var bodyParser = require('body-parser')

var bunyan = require('bunyan');

var logger = bunyan.createLogger({name: 'web-ui'});

var router = Router();    // create a new Router instance
var route = router.push;  // shortcut for router.push()
route('POST', bodyParser.urlencoded({extended: false}));
route('POST', bodyParser.json());

var elasticsearch = require('elasticsearch');
var client = new elasticsearch.Client({
	host: 'elastic.jx.35.233.117.189.nip.io',
	log: 'info'
  });

route('/stations', stations)
route('POST', '/timesheets_for', timesheets_for)
route('/', index)
var server = http.createServer(router).listen(8080);

function isObject(val) {
    if (val === null) { return false;}
    return ( (typeof val === 'function') || (typeof val === 'object') );
}

client.ping({
	requestTimeout: 30000,
  }, function (error) {
	if (error) {
		logger.error('elasticsearch cluster is down!');
	} else {
		logger.info('elasticsearch cluster is CONNECTED!');
	}
  });

let NAMES = []

// get the stations
logger.info("getting stations list from Elastic")
client.search({
	index: "timesheets_backup",
	type: "route_schedules",
	size: 1000,
	body: {
		"_source":["table.rows.stop_point.name"],
		"query": {
		  "match_all": {}
		}		
	}
}).then(response => {
	function find(returned, source, field) {
		for (let key of Object.keys(source)) {
			let fieldValue = source[key]
			if(field===key) {
				if(returned.indexOf(fieldValue)<0) {
					returned.push(fieldValue)
				}
			} else if(isObject(fieldValue)) {
				find(returned, fieldValue, field)
			}
		}
		return returned
	}
	logger.info("analyzing responses ...")
	find(NAMES, response.hits.hits, "name")
	NAMES.sort()
	logger.info({"message":"all stations are known", "body":{"count":NAMES.length}})
}).catch(error => logger.error({"message":"Unable to read stations list from Elastic","cause":error}))

function stations(req, res, next) {
	res.send(NAMES)
}

function timesheets_for(req, res, next) {
	logger.info({"message":"getting timesheets for", "body":req.body})
	let date = moment(req.body.date)
	// Reformat date in a way Elastic understands
	let formatted = date.toISOString().
		replace(/-/g, '').
		replace(/:/g, '').
		replace(/\..+/, '')     // delete the dot and everything after
	logger.info(`parsed ${date} into ${formatted}`)
	client.search({
		index: "timesheets_backup",
		type: "route_schedules",
		size: 1000,
		body:  {
			"_source":[
			  "display_informations.direction", 
			  "table.rows.stop_point.name",
			  "table.rows.date_times.date_time",
			  "table.rows.date_times.links.id",
			  "id"
			  ],
			"query": { 
			  "bool": { 
				"must": [
				  { 
					"match": { 
					  "table.rows.stop_point.name": req.body.station
					  }
				  }
				],
				"filter": [ {
					"range": {
					  "table.rows.date_times.date_time": {
						"gte": formatted
					  }
					}
				  }
				]
			  }
			}
		}
	}).then(response => {
		let returned = []
		// Now it's time to parse that deadly response (and filter out uninteresting elements - like already gone trains)
		for (let schedule of response.hits.hits) {
			let direction = schedule._source.display_informations.direction
			logger.info(`testing ${direction}`)
			for(let row of schedule._source.table.rows) {
				let stop = row.stop_point.name
				if(stop===req.body.station) {
					logger.info(`howdy ! schedule ${direction} stops at ${stop}. But when ?`)
					for(let datetime of row.date_times) {
						let instant = moment(datetime.date_time)
						let id = datetime.links[0].id
						if(instant.isBefore(date)) {
							// Nothing to do, we just go to the next
							logger.info(`There is a stop at ${instant}, before ${date}`)
						} else {
							logger.info(`there is a stop in journey ${id} at ${instant}. It is after our date (${date})!`)
							// Pushing infos into output
							returned.push({
								id: id,
								destination: direction,
								date: instant
							})
							// and checking next schedule
							break;
						}
					}
				}
			}
		}
		// Finally sort them on server side, just because
		returned.sort(function(a,b) {
			if(a.date.isBefore(b.date)) {
				return -1
			} else if(b.date.isBefore(a.date)) {
				return 1
			} else {
				return 0
			}
		})
		logger.info(returned)
		res.send(returned)
	}).catch(error => logger.error({"message":"Unable to read timesheets list from Elastic", "body":req.body, "cause":error}))
}

function index(req, resp, next) {
	fileSystem.readFile('./index.html', function(error, fileContent){
		if(error){
			resp.writeHead(500, {'Content-Type': 'text/plain'});
			resp.end('Error');
		} else{
			resp.writeHead(200, {
				'Content-Type': 'text/html',
//				'Access-Control-Allow-Origin': '*',
//				'Access-Control-Allow-Methods': 'POST, GET',
			});
			resp.write(fileContent);
			resp.end();
		}
	});
}