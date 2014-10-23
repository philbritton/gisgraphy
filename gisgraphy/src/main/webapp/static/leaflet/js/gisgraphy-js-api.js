autocompleteGisgraphyCounter = 0;
autocompleteGisgraphy=[];

currentRequests = {};

defaultAjax = {
beforeSend: function(request){
 if (currentRequests["geocoding"]) {
                                        try { currentRequests["geocoding"].abort();console.log('aborting'); } catch(e)    {console.log(e); }
                                }

currentRequests["geocoding"] = request;

},
complete: function(xhr){
currentRequests["geocoding"] = null;

}
// ......
};
function detectLanguage(){
	var lang = (navigator.language) ? navigator.language : navigator.userLanguage; 
	if (lang){lang=lang.split('-')[0]}
	return lang? lang.toUpperCase():"EN";
}

$( document ).ajaxStart(function() {
console.log( "Triggered ajaxStart handler." );
$('#gisgraphy-leaflet-searchButton').css("background-image","url(img/loading.gif)");
$('#gisgraphy-leaflet-searchButton').addClass("searching");
});

$( document ).ajaxStop(function() {
console.log( "Triggered ajaxStop handler." );
$('#gisgraphy-leaflet-searchButton').css("background-image",'url("img/search.png")');
$('#gisgraphy-leaflet-searchButton').removeClass("searching");
});


function setSearchText(htmlId,text){
	$('#'+htmlId).val(text);
}

var marker = undefined;
function moveCenterOfMapTo(lat,lng,placetype){
if (typeof map != 'undefined'){
	if ( lat != undefined && lng != 'undefined'){
               map.panTo(new L.LatLng(lat,lng));
		if (typeof marker != 'undefined'){
			map.removeLayer(marker)	      
		}
		marker = L.marker([lat, lng]).addTo(map); 
	}
	if (typeof placetype != 'undefined'){
		map.setZoom(getZoomByPlaceType(placetype));
	}
}
}

function getZoomByPlaceType(placetype){
 var zoom = 14;//for city and other
console.log(placetype);
 if (typeof placetype != 'undefined'){
       if (placetype.toUpperCase() == 'STREET'){
          zoom = 18;
      } else if (placetype.toUpperCase() == 'CITY'){
          zoom = 12;
      }  else if (placetype.toUpperCase() == 'ADM'){
          zoom = 9;
 }
}
return zoom;
}

 DEFAULT_LANGUAGE=detectLanguage();


 (function(root) {
        "use strict";
	if (typeof console == "undefined") {
	this.console = { log: function (msg) { /* do nothing since it would otherwise break IE */} };
	}
	var VERSION = "1.0.0";
        var old;
        old = root.gisgraphyAutocomplete;
        root.gisgraphyAutocomplete = gisgraphyAutocomplete;
        function gisgraphyAutocomplete(o) {
	   
	    if (!o) {
                $.error("usage : gisgraphyAutocomplete({ELEMENT_ID:'foo'})");
            }
            if (!o || !o.ELEMENT_ID) {
                $.error("please specify an ELEMENT_ID option");
            }
	
	    //user options
	    this.geocoding;
            this.autocompleteGisgraphyCounter= autocompleteGisgraphyCounter+'';
	    this.ELEMENT_ID = o.ELEMENT_ID;
            this.currentLanguage = (o.currentLanguage || DEFAULT_LANGUAGE).toUpperCase();
	    this.allowPoiSelection = o.allowPoiSelection || true;
	    this.allowMagicSentence = o.allowMagicSentence || true;
	    this.allowLanguageSelection = o.allowLanguageSelection || true;
            this.fulltextURL = o.fulltextURL || '/fulltext/suggest';
            this.reversegeocodingUrl = o.reversegeocodingUrl || '/reversegeocoding/search';
	    this.geocodingUrl = o.geocodingUrl || '/geocoding/search';
            this.enableReverseGeocoding = o.enableReverseGeocoding || true;//todo if enable check reversegeocodingUrl is defined
            this.limit=o.limit || 20;
	    this.apiKey=o.apiKey || undefined;
            this.formNodeID = o.formNodeID || this.ELEMENT_ID+'-form';
            this.toolsNodeID = o.toolsNodeID || this.ELEMENT_ID+'-tools';
            this.placetypeNodeID = o.placetypeNodeID || this.ELEMENT_ID+'-placetypes';
            this.languagesNodeID = o.languagesNodeID || this.ELEMENT_ID+'-languages';
            this.inputSearchNodeID = o.inputSearchNodeID || this.ELEMENT_ID+'-inputSearch';
	    this.searchButtonNodeID = o.searchButtonNodeID || this.ELEMENT_ID+'-searchButton';
	    this.resultBoxNodeID = o.resultBoxNodeID || this.ELEMENT_ID+'-resultBox';

	    this._formNode=undefined;
            this._toolsNode = undefined;
            this._placetypeNode = undefined;
            this._languagesNode = undefined;
            this._inputSearchNode = undefined;
            this._searchButtonNode = undefined;
            this._resultBoxNode = undefined;

	    this.buildSearchBox=buildSearchBox;
	    this.buildPlaceTypeDropBox=buildPlaceTypeDropBox;
	    this.BuildLanguageSelector=BuildLanguageSelector;
            this.buildPoisArray=buildPoisArray;
            this.initUI=initUI;
	    this.initAutoCompletion=initAutoCompletion;
	    this.pois = buildPoisArray(DEFAULT_LANGUAGE);
	    this.getLocalSuggestionsArray=getLocalSuggestionsArray;
	    this.changeLanguage=changeLanguage;
	    this.replace=$.proxy(replace,this);
	    this.doGeocoding = o.doGeocoding || doGeocoding;
	    this.doProcessGeocodingResults = o.doProcessGeocodingResults || $.proxy(doProcessGeocodingResults,this);
	    //used to know what to do when enter is press whether an itme is selected or not
	    this.itemSelected=false;
	    this.result=undefined;
	    this._detectPosition = o.detectPosition || detectPosition
            this._fillPosition = $.proxy(fillPosition,this);
            this.userLat=50.455;//undefined;
            this.userLng=3.204;//undefined;
            this.allowUserPositionDetection = o.allowUserPositionDetection || true;
            this.withHelp = o.withHelp || true;
	    this.displayHelp = displayHelp;
	    this.initUI();
	   
	   this.geocoding = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.nonword('name'),
  queryTokenizer: Bloodhound.tokenizers.nonword,
  limit : this.limit,
  local: this.getLocalSuggestionsArray(DEFAULT_LANGUAGE),
  remote:{
		url:this.fulltextURL +'?suggest=true&allwordsrequired=false&q=%QUERY',
		replace:this.replace,
		ajax:defaultAjax,
  		filter: function(d,e) {
			var names = [];
			if (d && d.response && d.response['docs']){
				$.each(d.response['docs'], function( key, value ) {
					if(value.name){
						names.push(value);
					}				
				});
			} else if (d && d.result && d.result[0]){
				names.push(convertAddress(d.result[0]));
			}
			return names;
			
		 },
 		rateLimitWait:130
	}
});


	    if (this.allowUserPositionDetection){
		this._detectPosition();
	    }
             function fillPosition(position){ 
			if (position){
			 this.userLat = position.coords.latitude;
			 this.userLng = position.coords.longitude;
			if (typeof map != 'undefined'){
				map.panTo(new L.LatLng(this.userLat,this.userLng));
			}
			 }
	     }
	     function detectPosition(){
	     try{	
			  navigator.geolocation.getCurrentPosition(this._fillPosition);
	      } catch (e) {
		console.log("can not detect position");
		console.log(e);
		 }
	    }
	    gisgraphyAutocomplete.normalize =  function normalize(input) {
	 	    $.each(charMap, function (unnormalizedChar, normalizedChar) {
	    	    var regex = new RegExp(unnormalizedChar, 'gi');
	    	    input = input.replace(regex, normalizedChar);
		 });
		 return input.replace(/\W+/,'');
	     }
	    Handlebars.registerHelper('if_eq', function(a, b, opts) {
		    if(a == b) 
			return opts.fn(this);
		    else
			return opts.inverse(this);
	    });

	   Handlebars.registerHelper('l10n', $.proxy(function(keyword) {
		var target = translation[keyword][this.currentLanguage];
		// fallback to the original string if nothing found
		target = target || keyword;	
		//output
		return target;
	    },this));

	    function buildSearchBox(){
	var box = $('<input>').attr('type','text').attr('placeholder',translation['placeholder'][DEFAULT_LANGUAGE]).attr('id',this.inputSearchNodeID).attr('name','q').attr('autocomplete','off').addClass('typeahead clearable searchbox').appendTo(this._formNode);
	var searchbutton =$('<input>').attr('type','button').attr('value','').addClass('searchbutton').attr('onclick','autocompleteGisgraphy['+autocompleteGisgraphyCounter+'].doGeocoding();').attr('id',this.searchButtonNodeID).appendTo(this._formNode);
	this._resultBoxNode = $('<div>').attr('id',this.resultBoxNodeID).addClass('resultBox').appendTo(this._formNode);
	if (this.withHelp == true){
		this.displayHelp();
	}
};

function displayHelp(){
var el = this._resultBoxNode;
if ($('#'+this.resultBoxNodeID).length >0){
 el =$('#'+this.resultBoxNodeID);
}
 el.html('<strong>Welcome to Gisgraphy !</strong><span class="closable" onclick="$(\'#'+this.resultBoxNodeID+'\').empty().hide();" >&nbsp;</span><br/>Gisgraphy is a free open source framework that provides 5 webservices (geocoding, reverse geocoding, find nearby, street search, fulltext / autocompletion / autosuggestion, address parsing).<ul><li> Up to house number, worldwide, internationalized</li><li> IT DOES ALL BY ITSELF, LOCALLY, no link to Google, yahoo, etc</li><li> It use free data (OpenstreetMap, Geonames, Quattroshapes,...) in its own database. </li><li>UI is modeled after google.com\'s search box</li></ul><br/>This leaflet plugin is kind of show case that use those webservices. try : <ul><li>A place : <a href="javascript:setSearchText(\''+this.inputSearchNodeID+'\',\'paris\')">paris</a>, <a href="javascript:setSearchText(\''+this.inputSearchNodeID+'\',\'big Apple\')">big apple</a></li><li>An address : <a href="javascript:setSearchText(\''+this.inputSearchNodeID+'\',\'Avenue des Champs-Élysées Paris\')">Avenue des Champs-Élysées Paris</a></li><li>A GPS : <a href="javascript:setSearchText(\''+this.inputSearchNodeID+'\',\'48.873409271240234,2.29619002342224\')">48.873409271240234,2.29619002342224</a></li><li>A DMS : <a href="javascript:setSearchText(\''+this.inputSearchNodeID+'\',\'40:26:46.302N 079:56:55.903W\')">40:26:46.302N 079:56:55.903W</a></li><li>A magic phrase : <a href="javascript:setSearchText(\''+this.inputSearchNodeID+'\',\'restaurant new york\')">restaurant new york</a></li></ul><a href="http://www.gisgraphy.com/"><span style="display:block;background-color:#2D81BE;height:25px;width:150px;margin:auto auto;text-align:center;color:#FFFFFF;font-size:1.2em;line_height:1.2em;vertical-align:middle;border-radius: 8px 8px 8px 8px;" ><b>Gisgraphy project &rarr;</b></span></a>');
if ($('#'+this.resultBoxNodeID).length >0){ 
	if ( $('#'+this.resultBoxNodeID ).is( ":hidden" ) ) {
		        $( '#'+this.resultBoxNodeID ).slideDown(200);
		} else {
		        $( '#'+this.resultBoxNodeID ).hide();
		}

		 $('#'+this.resultBoxNodeID).slideDown(200);
	}
}
 function replace() {
			if (this.enableReverseGeocoding){
				var latLong = convertToLatLong($('#'+this.inputSearchNodeID).val());
				if (latLong){
					return this.reversegeocodingUrl +"?format=json&lat="+latLong.lat+"&lng="+latLong.long;
				} 
			}	
			var fulltextUrlWithParam =  this.fulltextURL +'?format=json&suggest=true&allwordsrequired=false&style=long'
			if (this.userLat && this.userLng){
				fulltextUrlWithParam = fulltextUrlWithParam+"&lat="+this.userLat+"&lng="+this.userLng+"&radius=0";
		}
			if(!$('#'+this.placetypeNodeID).val()){
				fulltextUrlWithParam = fulltextUrlWithParam+'&placetype=city&placetype=adm&placetype=street';
			}
			fulltextUrlWithParam = fulltextUrlWithParam+"&from=1&to=10&"+ $('#'+this.formNodeID).serialize();
			 if (this.apiKey != undefined){
		            fulltextUrlWithParam = fulltextUrlWithParam+'&apikey='+this.apiKey;
        		}

			return fulltextUrlWithParam;
		}
;
function doGeocoding(){
	if (!$('#'+this.inputSearchNodeID).val()){
	 	alert(translation['placeholder'][this.currentLanguage]);
		return;
	}
	var url = this.geocodingUrl+'?format=json&address='+$('#'+this.inputSearchNodeID).val();
	 if (this.enableReverseGeocoding){
                                var latLong = convertToLatLong($('#'+this.inputSearchNodeID).val());
                                if (latLong){
                                        url =  this.reversegeocodingUrl +"?format=json&lat="+latLong.lat+"&lng="+latLong.long;
					if (this.apiKey != undefined){
					 url = url+'&apikey='+this.apikey
					}
                                }
         } 
	 if (this.apiKey != undefined){
	    url = url+'&apikey='+this.apikey;
 	}
	$.ajax({
	  url: url,
	})
	  .done($.proxy(doProcessGeocodingResults,this));
};
function doProcessGeocodingResults(data){
	    if ( console && console.log ) {
//	      console.log(data.result );
	    }
	    
	    $('#'+this.resultBoxNodeID).empty();
	     if (data){
		$('<div>').html('<strong></strong><span class="closable" onclick="$(\'#'+this.resultBoxNodeID+'\').empty().hide();" >&nbsp;</span><br/>').appendTo('#'+this.resultBoxNodeID);
		if (data.numFound && data.numFound > 0){  
			$.each(data.result,
				$.proxy(function( index, value ) {
					//console.log(value);
					var content ='';
					var hasName=false;
					if (value.countryCode){
					content+='<img src="img/'+value.countryCode+'.png" alt="'+value.countryCode+'" class="flag-autocomplete"/>';
					}
                                        if (value.houseNumber){
					content+=value.houseNumber+" ";	
					}
					if (value.streetName){
						hasName=true;
						content+="<strong>"+value.streetName+"</strong>";
					} else if (value.name){
						hasName=true;
						content+="<strong>"+value.name+"</strong>";
					}
					if (value.city){
						if (hasName == true){
							content+=', ';
						}
						content+='<span class="isin-autocomplete">'+value.city+'</span>';
					}
					if (value.lat && value.lng){
						content+="<br/>("+value.lat+","+value.lng+")";
					}
		  			$('<div onclick="moveCenterOfMapTo('+value.lat+','+value.lng+','+value.placetype+')">').html(content).appendTo('#'+this.resultBoxNodeID);
					if (index+1 < data.result.length){
						$('<hr>').appendTo('#'+this.resultBoxNodeID);
					}
	    				}
				,this)
			);
		} else {
			$('<div>').text('sorry no result found').appendTo('#'+this.resultBoxNodeID);
		}
	    } else {
		$('<div>').text('sorry no data recieved').appendTo('#'+this.resultBoxNodeID);
	  }
	 if ( $('#'+this.resultBoxNodeID ).is( ":hidden" ) ) {
		$( '#'+this.resultBoxNodeID ).slideDown(200);
	} else {
		$( '#'+this.resultBoxNodeID ).hide();
	}	

	 $('#'+this.resultBoxNodeID).slideDown(200);
	};




function buildPlaceTypeDropBox(lang){
	if (!lang){
	 lang=DEFAULT_LANGUAGE;
	}
	$('#lang'+lang).attr('checked','true');
	lang=lang.toUpperCase(); 
	var dropBoxHtml = $("#"+this.placetypeNodeID);
	var sel = $('<select>').attr('id',this.placetypeNodeID).attr('name','placetype').addClass('placetypes');
	if (dropBoxHtml.length > 0){
		dropBoxHtml.replaceWith(sel);
	} else {
		sel.appendTo(this._toolsNode)
	}

	sel.append($("<option>").attr('value','').text(translation['choosepoitype'][lang]));
	$.each(placetype, function( placetype, value ) {
	//	console.log( index + ": " + value );
		$.each(value, function( countrycode, translations ) {
			if (countrycode == lang ){
				var selectOptionText=placetype;
				if(translations.length >=1){
					selectOptionText= translations[0];
				} 
					//console.log( placetype+'['+countrycode+']' + "=" + selectOptionText );
					sel.append($("<option>").attr('value',placetype).text(selectOptionText));
			}
			//var value= index;
	
	});
	});
};

function BuildLanguageSelector(lang){
	var ff = function( key, value ) {
		$('<input>').attr('type','radio').attr('name','lang').attr('onclick','autocompleteGisgraphy['+autocompleteGisgraphyCounter+'].changeLanguage(this.value);').attr('id','lang'+key).attr('value',key).addClass('languages').appendTo(this._toolsNode).after(value);
	}
	$.each(SUPPORTED_LANGUAGE,$.proxy(ff,this ));
	 $('#lang'+lang).attr('checked','true');
        lang=lang.toUpperCase();

};

function buildPoisArray(lang){
	if (!lang){
	 lang=DEFAULT_LANGUAGE;
	}
	var pois=[];
	var seen = {};
	$.each(placetype, function( placetype, value ) {
			$.each(value, function( countrycode, translations ) {
				if (countrycode == lang ){
					if(translations.length == 0){
						//no translations
						pois.push(placetype);
					} else {
						var selectOptionText=placetype;
						if(translations.length > 0) {
							 for (var i = 0, len = translations.length; i < len; i++) {
								if (!seen[translations[i]]) {
								    seen[translations[i]] = true;
								    pois.push({"text":translations[i],"poiType":placetype});
								}
					    		}
						}
					}
				}
			});
	});
	return pois;
}

function initUI(){
this._formNode = $('<form>').attr('id',this.formNodeID).attr('action',this.geocodingUrl);//.appendTo('#'+this.ELEMENT_ID);
this._toolsNode = $('<div>').attr('id',this.toolsNodeID).addClass('tools').appendTo(this._formNode);
if (this.withHelp){
        $("<img>").attr('alt','help').attr('src','./img/help.png').attr('onclick','autocompleteGisgraphy['+autocompleteGisgraphyCounter+'].displayHelp();').addClass('help').appendTo(this._toolsNode);
}
if(this.allowLanguageSelection){
	this.BuildLanguageSelector(DEFAULT_LANGUAGE);
}
if(this.allowPoiSelection){
	this.buildPlaceTypeDropBox(DEFAULT_LANGUAGE);
}
this.buildSearchBox();
if ($('#'+this.ELEMENT_ID).length >0){
	    this._formNode.appendTo($('#'+this.ELEMENT_ID));
}
if ( this.userLat != undefined && this.userLng != 'undefined' && typeof map != 'undefined'){
                                map.panTo(new L.LatLng(this.userLat,this.userLng));

}
}



function initAutoCompletion(){
if ($('#'+this.ELEMENT_ID).length == 0){
	return;
}
this.geocoding.initialize();
$('#'+this.inputSearchNodeID).typeahead({
  hint: true,
  highlight: true,
  minLength: 1
},
{
  name: this.ELEMENT_ID+'',
  displayKey: function(obj){
				var is_in='';
				if (obj['is_in'] || obj['adm1_name'] ){
					if (obj['is_in']){
					is_in=obj['is_in']+', ';
					} /*else if (obj['adm1_name']){
					is_in=obj['adm1_name'];
					}*/
					return obj['name']+is_in;
				} else {
					return obj['name']
				}
				},
  // `ttAdapter` wraps the suggestion engine in an adapter that
  // is compatible with the typeahead jQuery plugin
  source: this.geocoding.ttAdapter(),
  templates: {
    empty: Handlebars.compile('<div class="empty-message">{{l10n "nosuggestion" currentLanguage}}</div>'),
    suggestion: Handlebars.compile('{{#if name}}<p>{{#if country_code}}<img src="img/{{country_code}}.png" alt={{country_code}} class="flag-autocomplete"/>{{#if houseNumber}} {{houseNumber}}</span>{{/if}} {{/if}}<strong>{{name}} {{#if_eq zipcode.length 1}}({{zipcode}}){{/if_eq}}</strong>{{#if is_in}} <span class="isin-autocomplete">, {{is_in}}</span> {{else}}{{#if adm1_name}} <span class="isin-autocomplete">, {{adm1_name}}</span>{{/if}}{{/if}}</p>{{/if}}'),
    footer: '<div class="footer">powered by <a href="http://www.gisgraphy.com/">Gisgraphy.com</a></div>'
  }
});

$('input.typeahead').keypress(
//$('#gisgraphy-leaflet-form').on('submit',
$.proxy(function (e) {
    if (e.which == 13){
		if(  !this.itemSelected) {
//        var selectedValue = $('input.typeahead').data().ttView.dropdownView.getFirstSuggestion().datum.id;
  //      $("#value_id").val(selectedValue);
	        console.log('enter pressed : '+this.itemSelected);
		this.doGeocoding();
        	$('#'+this.inputSearchNodeID).typeahead('close');
//        $('#gisgraphy-leaflet-form').submit();
		this.itemSelected=false;
        	return false;
    }
 else {
this.itemSelected=false;
}
}
},this));

$('#'+this.inputSearchNodeID).bind('typeahead:selected', doOnChoose);
$('#'+this.inputSearchNodeID).bind('typeahead:cursorchanged', doOnChoose);
$('#'+this.inputSearchNodeID).bind('typeahead:autocompleted', doOnChoose);
$('#'+this.inputSearchNodeID).focus();
}

	this.initAutoCompletion();
window.autocompleteGisgraphy[autocompleteGisgraphyCounter]=this;
autocompleteGisgraphyCounter++;

var doOnChoose= $.proxy(function(obj, datum, name) {
console.log('doOnChosse');
         if (datum && datum.poiType && this.allowPoiSelection) {
                         $('#'+this.placetypeNodeID+' option[value="'+datum.poiType+'"]').prop('selected', true);
                }else {
		$('#'+this.placetypeNodeID+' option[value=""]').prop('selected', true);
}
/*if (typeof map != 'undefined' && datum && datum.lat && datum.lng){
		console.log('moving to '+datum.lat+','+datum.lng);
                                map.panTo(new L.LatLng(datum.lat,datum.lng));
}*/
	moveCenterOfMapTo(datum.lat,datum.lng,datum.placetype);
        this.itemSelected=true;
        console.log('selected');
        console.log(obj);
        console.log(datum);
        console.log(name);
        this.result=datum
        return false;

},this);
 function tog(v){return v?'addClass':'removeClass';} 
  
  $(document).on('input', '.clearable', function(){
    $(this)[tog(this.value)]('x');
  }).on('mousemove', '.x', function( e ){
    $(this)[tog(this.offsetWidth-18 < e.clientX-this.getBoundingClientRect().left)]('onX');   
  }).on('click', '.onX', function(){
    $(this).removeClass('x onX').val('');
    $('geocoding').typeahead('val','');
    $('geocoding').typeahead('close');
  });

function getLocalSuggestionsArray(lang){
	if (this.allowMagicSentence){
		var poiArray = $.map(this.pois, function(poi) { return { name: poi.text+" "+ translation['near'][lang]+" ", poiType: poi.poiType, country_code:"gps" }; })
		var streetArray= $.map(streettype[lang], function(st) { return { name: st,country_code:"road" }; })
		return $.merge(poiArray,streetArray);
	}else {
	 return [];
	}
}

function changeLanguage(lang){
	this.currentLanguage=lang;
	if (this.allowPoiSelection){
		this.buildPlaceTypeDropBox(lang);
		this.pois = buildPoisArray(lang);
	}
	this.geocoding.clear();
	this.geocoding.local= this.getLocalSuggestionsArray(lang);
	this.geocoding.initialize(true);
	$('#'+this.inputSearchNodeID).focus();
	$('#'+this.inputSearchNodeID).attr('placeholder',translation['placeholder'][lang])
	
}


function convertAddress(address){
var doc={};
if (address){
	doc = {"name":address.streetName,
              "is_in":address.city,
              "country_code":address.countryCode,
              "lat":address.lat, 
	      "lng":address.lng,
              "distance":address.distance,
              "houseNumber":address.houseNumber};
}
	return doc;
}
  
        }
 
        gisgraphyAutocomplete.noConflict = function noConflict() {
            root.gisgraphyAutocomplete = old;
            return gisgraphyAutocomplete;
        };
        return gisgraphyAutocomplete;
	function getSorter(sortFn) {
            return sort ;
            function sort(array) {
                return array.sort(sortFn);
            }
            function noSort(array) {
                return array;
            }
        }
        function ignoreDuplicates() {
            return false;
        }
    })(this);


//-------------------------------------------------------------------------------------------
//DMS

/*
 Pattern 1 (ex: 40:26:46.302N 079:56:55.903W)
 Pattern 2 (ex: 40°26′47″N 079°58′36″W or 40°26'47"N 079°58'36"W)
 Pattern 3 (ex: 40d 26′ 47″ N 079d 58′ 36″ W or 40d 26' 47" N 079d 58' 36" W)
*/

function convertDMS(input){
var obj;
var matches = input.match(/((\d+)\s?[:°dD]*\s?(\d+)\s?\s?[:'′]*\s?(\d+([.]\d+)?)?\s?[:\"″]*\s?([NnSs])\s?([,;]\s?)?(\d+)\s?[:°dD]*\s?(\d+)\s?[:'′]*\s?(\d+([.]\d+)?)?\s?[:\"″]*\s?([EeWw]))/);
if (matches){
	/*for (var i=0;i<matches.length;i++){
		console.log(matches[i]);
	}*/
	lat = convertToDecimal(matches[2],matches[3],matches[4],matches[6]);
	long = convertToDecimal(matches[8],matches[9],matches[10],matches[12]);
	obj =  {"lat":lat,"long":long};
}
return obj
}


function convertToDecimal(
degrees, minutes, seconds, hemisphere){

var hemi = (hemisphere.toUpperCase() === "N"
|| hemisphere.toUpperCase() === "E") ? 1 : -1;

degrees=parseFloat(degrees.replace(',', '.'))
minutes=parseFloat(minutes.replace(',', '.'))
seconds=parseFloat(seconds.replace(',', '.'))

return (degrees + minutes / 60.0 + seconds / 3600.0) * hemi ;
}

function couldbeCoordinate(str){
	match =  str.match(/[-\d\sWwEeNnSsOo\.,:°'"]+/);
	if (match){
		return match[0]==str;
	} else {
		return false;
	}
}



/* Pattern 1 (ex: -23.399437,-52.090904 or 40.446195, -79.948862)
 Pattern 2 (ex: 40.446195N 79.948862W)
*/
function convertDD(input){
	var obj;
	var matches = input.match(/((-?\d+[.,]\d+)\s*([NnSs])?(\s*[,;]?\s*)?(-?\d+[,.]\d+)\s*([WwEe])?)/);
	if (matches){
		/*for (var i=0;i<matches.length;i++){
			console.log(matches[i]);
		}*/
		lat = parseDD(matches[2],matches[3]);
		long = parseDD(matches[5],matches[6]);
		obj =  {"lat":lat,"long":long};
	}
		return obj;
}


function parseDD(decimalDegrees, hemisphere){
	var hemi = 1;
	if(hemisphere){
		 hemi = (hemisphere.toUpperCase() === "N"
		|| hemisphere.toUpperCase() === "E") ? 1 : -1;
	}

	decimalDegrees=parseFloat(decimalDegrees.replace(',', '.'));

return decimalDegrees * hemi ;
}

/*convertDMS("40:26:46.302N 079:56:55.903W");
convertDMS(50°37'59.7"N 3°03'13.2"E")
convertDD("-23.399437,-52.090904");
convertDD("-23.399437 -52.090904");
convertDD("-23,399437  -52,090904");*/

function convertToLatLong(str){
	var obj;	
	if(str){
		if (couldbeCoordinate(str)){
			obj= convertDD(str);
			if(!obj){
				obj = convertDMS(str)
			}
		}	
	}
	return obj;
}

