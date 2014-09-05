var eb = require('vertx/event_bus');

eb.registerHandler('navigation', function(message) {
	document.getElementById("main-content").innerHTML='<object type="text/html" data="test.html" ></object>';
});