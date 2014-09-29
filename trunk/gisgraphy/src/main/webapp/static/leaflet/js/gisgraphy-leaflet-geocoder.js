L.Control.gisgraphygeocoder = L.Control.extend({
options: {
position: 'topleft',
},

initialize: function (options) {
	L.Util.setOptions(this, options);
},
onAdd: function (map) {
this._map = map;
//var className = 'leaflet-control-geocoder',
container = this._container = L.DomUtil.create('div');
$('<div>').attr('id','gisgraphy-leaflet').appendTo('#map');
container.id="gisgraphy-leaflet";
this.gg = new gisgraphyAutocomplete(this.options);
//console.log($('#gisgraphy-leaflet'));
//gg.initUI();
console.log(container);
this.gg.initUI();
console.log(container);
return container;
},



onRemove: function (map) {
/*map.off('mousemove', this._onMouseMove)*/
},

});

/*
L.Map.addInitHook(function () {
if (this.gg) {
console.log($('#gisgraphy-leaflet'));
this.gg.initUI();

}
});
*/
/*L.control.gisgraphygeocoder = function (options) {
if (options || options.ELEMENT_ID) {
                throw new Error("please specify an ELEMENT_ID option");
        }

	return new L.Control.gisgraphygeocoder(options);
};*/
