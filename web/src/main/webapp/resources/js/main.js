jQuery(document).ready(function($) {

    $("#exampleModal").on('show.bs.modal', function(e) {
        var currencyId = $(e.relatedTarget).data('currency-id');

        var cols = $('#currency-' + currencyId + ' td');
        var name = $(cols[0]).text();
        var code = $(cols[1]).text();
        var symbol = $(cols[2]).text();

        $('#name').val(name);
        $('#code').val(code);
        $('#symbol').val(symbol);
        $("#currencyForm").attr("action", $('#currencyForm').attr("action") + "?currencyId=" + currencyId);
    });

    $(".modal-header").on("mousedown", function(mousedownEvt) {
        var $draggable = $(this);
        var x = mousedownEvt.pageX - $draggable.offset().left,
            y = mousedownEvt.pageY - $draggable.offset().top;
        $("body").on("mousemove.draggable", function(mousemoveEvt) {
            $draggable.closest(".modal-dialog").offset({
                "left": mousemoveEvt.pageX - x,
                "top": mousemoveEvt.pageY - y
            });
        });
        $("body").one("mouseup", function() {
            $("body").off("mousemove.draggable");
        });
        $draggable.closest(".modal").one("bs.modal.hide", function() {
            $("body").off("mousemove.draggable");
        });
    });

});