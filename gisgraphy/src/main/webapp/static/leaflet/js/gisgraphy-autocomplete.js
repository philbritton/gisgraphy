function detectLanguage(){
	var lang = (navigator.language) ? navigator.language : navigator.userLanguage; 
//	console.log(lang) ;
	return lang? lang.toUpperCase():"EN";
}

 DEFAULT_LANGUAGE=detectLanguage();


 (function(root) {
        "use strict";
	if (typeof console == "undefined") {
	this.console = { log: function (msg) { /* do nothing since it would otherwise break IE */} };
	}
	var VERSION = "1.0.0";
        var old, keys2;
        old = root.gisgraphyAutocomplete;
        keys2 = {
            data: "data",
            protocol: "protocol",
            thumbprint: "thumbprint"
        };
        root.gisgraphyAutocomplete = gisgraphyAutocomplete;
        function gisgraphyAutocomplete(o) {
	   
	    if (!o) {
                $.error("usage : gisgraphyAutocomplete({ELEMENT_ID:'foo'})");
            }
            if (!o || !o.ELEMENT_ID) {
                $.error("please specify an ELEMENT_ID option");
            }
	    //user options
	    this.ELEMENT_ID = o.ELEMENT_ID;
            this.currentLanguage = (o.currentLanguage || DEFAULT_LANGUAGE).toUpperCase();
	    this.allowPoiSelection = o.allowPoiSelection || true;
	    this.allowLanguageSelection = o.allowLanguageSelection || true;
            this.fulltextURL = o.fulltextURL || '/fulltext/suggest';
            this.reversegeocodingUrl = o.reversegeocodingUrl || '/reversegeocoding/search';
	    this.geocodingUrl = o.geocodingUrl || '/geocoding/search';
            this.enableReverseGeocoding = o.enableReverseGeocoding || true;//todo if enable check reversegeocodingUrl is defined
            this.limit=o.limit || 20;
            this.onItemSelect=o.onItemSelect || logOnSelect;

            this.formNodeID = o.formNodeID || this.ELEMENT_ID+'-form';
            this.placetypeNodeID = o.placetypeNodeID || this.ELEMENT_ID+'-placetypes';
            this.languagesNodeID = o.languagesNodeID || this.ELEMENT_ID+'-languages';
            this.inputSearchNodeID = o.inputSearchNodeID || this.ELEMENT_ID+'-inputSearch';
	    this.searchButtonNodeID = o.searchButtonNodeID || this.ELEMENT_ID+'-searchButton';
	    this.resultBoxNodeID = o.resultBoxNodeID || this.ELEMENT_ID+'-resultBox';
	    this.buildSearchBox=buildSearchBox;
	    this.buildPlaceTypeDropBox=buildPlaceTypeDropBox;
	    this.BuildLanguageSelector=BuildLanguageSelector;
            this.buildPoisArray=buildPoisArray;
            this.initUI=initUI;
	    this.pois = buildPoisArray(DEFAULT_LANGUAGE);
	    this.getLocalSuggestionsArray=getLocalSuggestionsArray;
	    this.changeLanguage=changeLanguage;
	    this.replace=$.proxy(replace,this);
	    this.doGeocoding = o.doGeocoding || doGeocoding;
	    this.doProcessGeocodingResults = o.doProcessGeocodingResults || $.proxy(doProcessGeocodingResults,this);
            this.initUI();
	    function logOnSelect(obj, datum, name) {      
                 console.log(obj); 
                 console.log(datum);
                 console.log(name); 
	     };
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
	var box = $('<input>').attr('type','text').attr('placeholder',translation['placeholder'][DEFAULT_LANGUAGE]).attr('id',this.inputSearchNodeID).attr('name','q').attr('autocomplete','off').addClass('typeahead clearable searchbox').appendTo('#'+this.formNodeID);
	var searchbutton =$('<input>').attr('type','button').attr('value','').addClass('searchbutton').attr('onclick','gg.doGeocoding();').attr('id',this.searchButtonNodeID).appendTo('#'+this.formNodeID);
	var resultsBox = $('<div>').attr('id',this.resultBoxNodeID).addClass('resultBox').appendTo('#'+this.formNodeID)
};
 function replace() {
			if (this.enableReverseGeocoding){
				var latLong = convertToLatLong($('#'+this.inputSearchNodeID).val());
				if (latLong){
					return this.reversegeocodingUrl +"?format=json&lat="+latLong.lat+"&lng="+latLong.long;
				} 
			}	
			return this.fulltextURL +'?format=json&suggest=true&allwordsrequired=false&radius=10000000&placetype=city&placetype=adm&style=long&placetype=street&lat=50.455&lng=3.204&from=1&to=50&'+ $('#'+this.formNodeID).serialize();
		}
;
function doGeocoding(){
	$.ajax({
	  url: this.geocodingUrl+'?format=json&address='+$('#'+this.inputSearchNodeID).val(),
	})
	  .done($.proxy(doProcessGeocodingResults,this));
};
function doProcessGeocodingResults(data){
	    if ( console && console.log ) {
	      console.log(data.result );
	    }
	    
	    $('#'+this.resultBoxNodeID).empty();
	     if (data){
		if (data.numFound && data.numFound > 0){  
			$.each(data.result,
				$.proxy(function( index, value ) {
		  			$('<div>').text(value.name+'('+value.lat+','+value.lng+')').appendTo('#'+this.resultBoxNodeID);
					$('<hr>').appendTo('#'+this.resultBoxNodeID);
	    				}
				,this)
			);
		} else {
			$('<div>').text('sorry no result found').appendTo('#'+this.resultBoxNodeID);
		}
	    } else {
		$('<div>').text('sorry no data recieved').appendTo('#'+this.resultBoxNodeID);
	  }
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
		sel.appendTo('#'+this.formNodeID)
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
		$('<input>').attr('type','radio').attr('name','lang').attr('onclick','gg.changeLanguage(this.value);').attr('id','lang'+key).attr('value',key).addClass('languages').appendTo('#'+this.formNodeID).after(value);
	}
	$.each(SUPPORTED_LANGUAGE,$.proxy(ff,this ));
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
								    pois.push(translations[i]);
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
$('<form>').attr('id',this.formNodeID).appendTo('#'+this.ELEMENT_ID);
if(this.allowLanguageSelection){
	this.BuildLanguageSelector(DEFAULT_LANGUAGE);
}
if(this.allowPoiSelection){
	this.buildPlaceTypeDropBox(DEFAULT_LANGUAGE);
}
this.buildSearchBox();
}

var geocoding = new Bloodhound({
  datumTokenizer: Bloodhound.tokenizers.obj.nonword('name'),
  queryTokenizer: Bloodhound.tokenizers.nonword,
  limit : this.limit,
  local: this.getLocalSuggestionsArray(DEFAULT_LANGUAGE),
  remote:{
		url:this.fulltextURL +'?suggest=true&allwordsrequired=false&q=%QUERY',
		replace:this.replace,
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
 		rateLimitWait:10
	}
});

geocoding.initialize();

$('#'+this.inputSearchNodeID).typeahead({
  hint: true,
  highlight: true,
  minLength: 1
},
{
  name: 'geocoding',
  displayKey: function(obj){
				var is_in='';
				if (obj['is_in'] || obj['adm1_name'] ){
					if (obj['is_in']){
					is_in=obj['is_in'];
					} else if (obj['adm1_name']){
					is_in=obj['adm1_name'];
					}
					return obj['name']+' , '+is_in;
				} else {
					return obj['name']
				}
				},
  // `ttAdapter` wraps the suggestion engine in an adapter that
  // is compatible with the typeahead jQuery plugin
  source: geocoding.ttAdapter(),
  templates: {
    empty: Handlebars.compile('<div class="empty-message">{{l10n "nosuggestion" currentLanguage}}</div>'),
    suggestion: Handlebars.compile('{{#if name}}<p>{{#if country_code}}<img src="img/{{country_code}}.png" alt={{country_code}} class="flag-autocomplete"/> {{/if}}<strong>{{name}} {{#if_eq zipcode.length 1}}({{zipcode}}){{/if_eq}}</strong>{{#if is_in}} <span class="isin-autocomplete">, {{is_in}}</span> {{else}}{{#if adm1_name}} <span class="isin-autocomplete">, {{adm1_name}}</span>{{/if}}{{/if}}</p>{{/if}}'),
    footer: '<div class="footer">powered by <a href="http://www.gisgraphy.com" target="gisgraphy">Gisgraphy</a></div>'
  }
});

$('#'+this.inputSearchNodeID).bind('typeahead:selected', function(obj, datum, name) {      
        console.log(obj); 
        console.log(datum);
        console.log(name); 

});

$('#'+this.inputSearchNodeID).focus();


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
	var poiArray = $.map(this.pois, function(poi) { return { name: poi+" "+ translation['near'][lang]+" ",country_code:"gps" }; })
	var streetArray= $.map(streettype[lang], function(st) { return { name: st,country_code:"road" }; })
	return $.merge(poiArray,streetArray);
}

function changeLanguage(lang){
	this.currentLanguage=lang;
	this.buildPlaceTypeDropBox(lang);
	this.pois = buildPoisArray(lang);
	geocoding.clear();
	geocoding.local= this.getLocalSuggestionsArray(lang);
	geocoding.initialize(true);
	$('#'+this.inputSearchNodeID).focus();
	$('#'+this.inputSearchNodeID).attr('placeholder',translation['placeholder'][lang])
	
}


function convertAddress(address){
	doc= {"name":address.streetName, "is_in":address.city, "country_code":address.countryCode, "lat":address.lat,"lng":address.lng,"distance":address.distance,"houseNumber":address.houseNumber};
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

