(function($) {

$.extend(Tapestry.Initializer, {
    dialogAjaxLink: function(spec) {
        var element = spec.element;
        var zoneId = spec.zoneId;
        var dialogId = spec.dialogId;
        var url = spec.url;
        var onOpen = function(event, ui) {
            $("#" + zoneId).tapestryZone("update", {
                url: url
            });
		};
		

        $("#" + element).click(function(e) {
			$('#' + dialogId).one("dialogopen", onOpen);
            $('#' + dialogId).dialog('open');
			
			return false;
        });
    }
});

})(jQuery);






