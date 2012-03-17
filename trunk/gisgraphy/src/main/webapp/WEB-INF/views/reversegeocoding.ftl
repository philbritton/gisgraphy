<#import "macros/utils.ftl" as utils>
<#import "macros/breadcrumbs.ftl" as breadcrumbs>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title><@s.text name="search.geocoding.reverse.title"/></title>
<meta name="Description" content="Worldwide Reverse geocoding free webservices and street search for openstreetmap. Pagination, indentation, several languages are supported"/>
<meta name="heading" content="<@s.text name="search.geocoding.reverse.title"/>"/>
<meta name="keywords" content=" reverse geocoding world worldwide street search free java openstreetmap webservices "/>
</head>
<body>
<br/>
<div id="gissearch">
<noscript>
	<div class="tip yellowtip">
	<@s.text name="global.noscript.required"/>
	</div>
	<br/>
</noscript>
	
			<@breadcrumbs.searchNavBar/>

<div class="clear"></div><div style="line-height:1.5em;">

<@s.text name="search.reversegeocoding.desc"/>.
 <@s.text name="search.openstreetmap.data"><@s.param>http://openstreetmap.org</@s.param></@s.text>.
 <@s.text name="search.geocoding.notUsinggooglemap"/>.
 <@s.text name="search.docandinstall">
 	<@s.param>http://www.gisgraphy.com/documentation/user-guide.htm#geocodingservice</@s.param>
 	<@s.param>http://www.gisgraphy.com/documentation/installation/index.htm</@s.param>
 </@s.text>.
</div><br/><br/>
<div class="clear"></div>


	<@s.form action="/street/search" method="get" id="reversegeocoding">
		<div id="simplesearch">
			<@gisgraphysearch.latlongsearchbox/>
			<@breadcrumbs.streetsearchTooltip advancedSearchURLParam="streetSearch"/>
<@s.hidden size="1" name="from" id="from"  value="1" theme="simple" />
<@s.hidden size="1" name="to"  id="to" value="1" theme="simple"/>
	</div>
	<div class="clear"><br/></div>
	</@s.form>
</div>
<div id="popupResults"></div>

<@utils.includeJs jsName="/scripts/prototype.js"/>
<@utils.includeJs jsName="/scripts/gisgraphyapi.js"/>

<script type="text/javascript" >
pointIsRequired=true;
getHtmlFromSelectedStreet = function(selectedStreetInformation){
var html = '<div id="EmplacementStreetView" class="googlemapInfoWindowHtml"><img src="/images/logos/logo_32.png" alt="free geocoding services" class="imgAlign"/><span  class="biggertext"><@s.text name="search.geocoding.services"/></span><hr/>id : <span  class="biggertext">'+selectedStreetInformation.gid+'</span><br/><span  class="biggertext">'+selectedStreetInformation.name+'</span><br/>';
if (selectedStreetInformation.streetType != null){html= html + "<@s.text name="search.type.of.street"/> : "+selectedStreetInformation.streetType+"<br/><br/>";}
 if (selectedStreetInformation.oneWay==true){html = html+ '<@s.text name="street.oneway" />'; } else { html = html +'<@s.text name="street.twoway" />';}
html= html +'<br/><br/> <@s.text name="global.latitude" /> : '+selectedStreetInformation.lat+'<br/><br/><@s.text name="global.longitude" /> : '+selectedStreetInformation.lng+'<br/><br/> <@s.text name="global.length" /> : '+(selectedStreetInformation.length*100000)+' m</div>';
return html;
}

doSearch = function(){
	if (checkParameters("reversegeocoding")== false){
		return false;
	}
	query = new GisgraphyQuery("reversegeocoding",function(response){
		var data = response.evalJSON(true);
		var results = data.result
		var resultsSize = results.length
		if (resultsSize == 0){
			alert('no result found');
		} else if (resultsSize == 1){
			selectedStreetInformation = results[0];
			viewStreet(selectedStreetInformation.lat,selectedStreetInformation.lng,getHtmlFromSelectedStreet(selectedStreetInformation));
			viewStreetPanorama(selectedStreetInformation.lat,selectedStreetInformation.lng);
		}
	}
);
query.execute();
return false;
}

</script>

<@gisgraphysearch.googleStreetView width="700" heigth="400" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />
<br/><br/>
<@gisgraphysearch.googleStreetPanorama width="700" heigth="300" 
	googleMapAPIKey=googleMapAPIKey CSSClass="center" />

</body>
</html>