gg = undefined;

L.Control.gisgraphygeocoder = L.Control.extend({
options: {
position: 'topleft',
},

initialize: function (options) {
	L.Util.setOptions(this, options);
},
onAdd: function (map) {
this._map = map;
container = this._container = L.DomUtil.create('div');
container.id="gisgraphy-leaflet";
gg = this.gg = new gisgraphyAutocomplete(this.options);
container.appendChild(this.gg._formNode[0]);
//container = gg._formNode[0];
return container;
},


initAutoCompletion : function(){
if (this.gg){
	this.gg.initAutoCompletion();
}
},
onRemove: function (map) {
/*map.off('mousemove', this._onMouseMove)*/
},

});



/*L.control.gisgraphygeocoder = function (options) {
if (options || options.ELEMENT_ID) {
                throw new Error("please specify an ELEMENT_ID option");
        }

	return new L.Control.gisgraphygeocoder(options);
};*/
