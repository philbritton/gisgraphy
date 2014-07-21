<#import "macros/utils.ftl" as utils>
<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.geocoding.title"/></title>
<meta name="Description" content="Worldwide geocoding free webservices and street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.geocoding.title"/>"/>
<meta name="keywords" content="geocoding world worldwide street search java openstreetmap webservices postgis hibernate toponyms gazeteers"/>
<@utils.includeJs jsName="/scripts/prototype.js"/>
</head>
<body onunload="GUnload()">
<br/>
<div id="gissearch">
<noscript>
	<div class="tip yellowtip">
	<@s.text name="global.noscript"/>
	</div>
	<br/>
</noscript>
	<@s.url id="geocodingFormUrl" action="geocoding" includeParams="none" method="search" namespace="/public" />
			
<@breadcrumbs.searchNavBar/>

<div  style="line-height:1.5em;">

<div class="clear"></div>
	
 <div class="example" id="remote">
<input type="text" placeholder="enter an address" id="autocomplete" class="typeahead"/>
</div>

<script src="http://localhost:8080/scripts/typeahead/jquery-1.11.1.js"></script>
<script src="http://localhost:8080/scripts/typeahead/typeahead.bundle.js"></script>
<script src="http://localhost:8080/scripts/typeahead/bloodhound.js"></script>
<script src="http://localhost:8080/scripts/typeahead/typeahead.jquery.js"></script>
<script src="http://localhost:8080/scripts/typeahead/handlebars-v1.3.0.js"></script>

<script type="text/javascript">

var pois = ['Court House','Craft','Customs Post','Dam','Dentist','Desert','Doctor','EmergencyPhone',
'Factory','Falls','Farm','Ferry Terminal','Field','Fire Station','Fishing Area','Fjord','Forest','Fountain',
'Fuel','Garden','Golf','Gorge','Grass Land','Gulf','Hill','Hospital','Hotel','House','Ice','Island','Lake',
'Library','Light House','Mall','Marsh','Metro Station','Military','Mill','Mine','Mole','Monument','Mound',
'Mountain','Museum','NightClub','Oasis','Observatory Point','Ocean','Opera House','Park','Parking','Pharmacy',
'Picnic','Plantation','PolicePost','Political Entity','Pond','Port','Post Office','Prison','Pyramid','Quay',
'Rail','RailRoad Station','Ranch','Ravin','Reef','Religious','Rental','Reserve','RestArea','Restaurant',
'Road','School','Sea','Shop','Sport','Spring','Stadium','Strait','Stream','Street','Swimming Pool','Taxi',
'Telephone','Theater','Toilet','Tourism','TourismInfo','Tower','Tree','Tunnel','UnderSea','Vending Machine',
'Veterinary','Vineyard','Volcano','Zoo','Bench','Cinema','Dentist','Doctor','EmergencyPhone','FerryTerminal',
'FireStation','Fountain','Fuel','NightClub','Pharmacy','Rental','Shop','Taxi','Telephone',
'Toilet','VendingMachine','Veterinary','Ambulance','Camping','Cave','CityHall','Craft','Picnic','RestArea',
'Sport','Tourism','Tourism Info'
];




var geocoding = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
  queryTokenizer: Bloodhound.tokenizers.whitespace,
  local: $.map(pois, function(poi) { return { name: poi+ ' near ' }; }),
  remote:{
		url:'http://localhost:8080/solr/suggest?indent=on&version=2.2&q=%QUERY&fq=&start=0&rows=10&fl=adm1_name,name&wt=json&explainOther=&hl.fl=&bq=population^2&fq=placetype:city placetype:adm&bf=pow(population,0.3) pow(city_population,0.3)',
  		filter: function(d,e) {
			var names = [];
			return d.response['docs'];
		 }
	}
});

// kicks off the loading/processing of `local` and `prefetch`
geocoding.initialize();

$('#autocomplete').typeahead({
  hint: true,
  highlight: true,
  minLength: 2
},
{
  name: 'geocoding',
  displayKey: 'name',
  // `ttAdapter` wraps the suggestion engine in an adapter that
  // is compatible with the typeahead jQuery plugin
  source: geocoding.ttAdapter(),
  templates: {
    empty: '<div class="empty-message">no results found</div>'
    ,suggestion: Handlebars.compile('<p><strong>{{name}}</strong> – {{adm1_name}}</p>')
  }
});



</script>

<style>
/*@font-face {
  font-family: Prociono;
  src: url(../font/Prociono-Regular-webfont.ttf);
}
*/
.tt-dropdown-menu,
.gist {
  text-align: left;
}

.typeahead,
.tt-query,
.tt-hint {
  width: 396px;
  height: 20px;
  padding: 4px 8px;
  font-size: 14px;
  line-height: 20px;
  border: 2px solid #ccc;
  -webkit-border-radius: 8px;
     -moz-border-radius: 8px;
          border-radius: 8px;
  outline: none;
}

.typeahead {
  background-color: #fff;
}

.typeahead:focus {
  border: 2px solid #1465b7;
}

.tt-query {
  -webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
     -moz-box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
          box-shadow: inset 0 1px 1px rgba(0, 0, 0, 0.075);
}

.tt-hint {
  color: #999
}

.tt-dropdown-menu {
  width: 422px;
  margin-top: 2px;
  padding: 0px 0;
  background-color: #fff;
  border: 1px solid #ccc;
  border: 1px solid rgba(0, 0, 0, 0.2);
  -webkit-border-radius: 8px;
     -moz-border-radius: 8px;
          border-radius: 8px;
  -webkit-box-shadow: 0 5px 10px rgba(0,0,0,.2);
     -moz-box-shadow: 0 5px 10px rgba(0,0,0,.2);
          box-shadow: 0 5px 10px rgba(0,0,0,.2);
}

.tt-suggestion {
  padding: 3px 20px;
  font-size: 12px;
  line-height: 24px;
}

.tt-suggestion.tt-cursor {
  color: #fff;
  background-color: #0097cf;

}

.tt-suggestion p {
  margin: 0;
}


/* example specific styles */
/* ----------------------- */

.empty-message {
  padding: 5px 10px;
 text-align: left;
 font-size: 14px;
}


</style>

</body></html>