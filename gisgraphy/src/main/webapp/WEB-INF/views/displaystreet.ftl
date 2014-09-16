<#import "macros/utils.ftl" as utils>
<#import "macros/gisgraphysearch.ftl" as gisgraphysearch>
<html>
<head>
<title>${preferedName}</title>
<meta name="Description" content="${preferedName}"/>
<meta name="heading" content="Free Geolocalisation Services"/>
<meta name="keywords" content="${preferedName} GPS information population elevation"/>
<@utils.includeJs jsName="/scripts/prototype.js"/>
<#if shape??>
 <script src='https://api.tiles.mapbox.com/mapbox.js/plugins/leaflet-omnivore/v0.2.0/leaflet-omnivore.min.js'></script>
</#if>

</head>
<body>
<br/>
			<div class="clear"><br/></div>
				<div class="bodyResults">
						<div>
						<span class="flag" >
							<img src="/images/flags/${result.countryCode}.png" alt="country flag"/>
						</span>
								<span class="resultheaderleft">${preferedName}</span>
						</div>
					
						<div class="separator"><hr/></div>
					
						<div class="summary">
						<@s.text name="global.latitude"/> : ${result.location.y}; 
						<br/>
						<@s.text name="global.longitude"/> : ${result.location.x}
						<br/>
						<@s.text name="global.length"/> : ${result.length} m(s); 
						<br/>
						<#if result.isIn??><@s.text name="global.is.in"/> : ${result.isIn};<br/></#if>
					        <#if result.isInPlace??><@s.text name="global.is.inplace"/> : ${result.isInPlace};<br/></#if>
					        <#if result.isInAdm??><@s.text name="global.is.inadm"/> : ${result.isInAdm};<br/></#if>
					        <#if result.isInZip??><@s.text name="global.is.inzip"/> : ${result.isInZip};<br/></#if>
						<br/>
						<#if result.openstreetmapId??><@s.text name="global.openstreetmapId"/> : ${result.openstreetmapId?c};<br/></#if>
						<br/>
						<#if result.streetType??><@s.text name="search.type.of.street"/> : <@s.text name="${result.streetType}"/><br/></#if>
						<#if result.one_way?? && result.length??>
							<#if result.one_way>
								<@s.text name="street.oneway"/>
							<#else>
								<img src="/images/twoway.png" class="imgAlign" alt="<@s.text name="global.street.way"/>"/>
								<@s.text name="street.twoway"/>
							</#if>
						<br/><br/>
					</#if>
						<#if result.houseNumbers??>
							<@s.text name="address.houseNumber"/> : 
							<#list result.houseNumbers as house>
							${house.number},
							</#list>;
							<br/><br/>
						</#if>
						 <#if result.alternateNames?? && result.alternateNames.size()!=  0>
                                       		 <p class="quote">
                                                <@s.iterator value="result.alternateNames" status="name_wo_lang_status" id="name_alternate">
                                                        ${name_alternate.name} <@s.if test="!#name_wo_lang_status.last"> - </@s.if>         
                                                </@s.iterator>
                                                </p>
                                       		 </#if>

						<#if shape??>
                           <div class="center"><strong><@s.text name="displaystreet.partofstreet"/></strong></div><br/>
                        </#if>

						
						<@gisgraphysearch.leafletMap width="700" heigth="400" 
						googleMapAPIKey=googleMapAPIKey CSSClass="center" />
						<br/><br/>
						<#--<@gisgraphysearch.googleStreetPanorama width="700" heigth="300" 
						googleMapAPIKey=googleMapAPIKey CSSClass="center" />-->
						<script type="text/javascript">
						
						function commadot(that) {
						    if (that.indexOf(",") >= 0) {
						       return that.replace(/\,/g,".");
						    }
						}
						 displayMap(commadot('${result.location.y + 0.0005}'),commadot('${result.location.x}'),"<strong>${preferedName}</strong><br/>Lat :${result.location.y}<br/>long:${result.location.x}");
                         <#if shape??>
                              omnivore.wkt.parse('${shape}').addTo(map);
                         </#if>
						</script>
						</div>
					</div>
					<div class="clear"></div>
					<br/><br/>



			 <div class="clear"><br/></div>
</body>
</html>