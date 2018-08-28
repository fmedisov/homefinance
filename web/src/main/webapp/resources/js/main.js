jQuery(document).ready(function($) {

    //Записываем в поля формы редактирования значения из таблицы выбранной валюты
    //todo реализовать в thymeleaf, если возможно
    $("#currencyModal").on('show.bs.modal', function(e) {
        var currencyId = $(e.relatedTarget).data('currency-id');
        if(typeof currencyId === "undefined")
            currencyId = null;

        var name = $('#currency-name-' + currencyId).text();
        var code = $('#currency-code-' + currencyId).text();
        var symbol = $('#currency-symbol-' + currencyId).text();

        $('#name').val(name);
        $('#code').val(code);
        $('#symbol').val(symbol);
        $("#currencyForm").attr("action", "/currency/submit" + "?currencyId=" + (currencyId ? currencyId : ""));
    });

    $("#categoryModal").on('show.bs.modal', function(e) {
        var categoryId = $(e.relatedTarget).data('category-id');
        if(typeof categoryId === "undefined")
            categoryId = null;

        var name = $('#category-name-' + categoryId).text();
        var parent = $('#category-name-' + categoryId).data('parent');

        $('#name').val(name);
        $('#parent').val(parent);
        $("#categoryForm").attr("action", "/category/submit" + "?categoryId=" + (categoryId ? categoryId : ""));
    });

    //Записываем в поля формы редактирования значения из таблицы выбранного счета
    $("#accountModal").on('show.bs.modal', function(e) {
        var accountId = $(e.relatedTarget).data('account-id');
        if(typeof accountId === "undefined")
            accountId = null;

        var name = $('#account-name-' + accountId).text();
        var accountType = $('#account-type-' + accountId).text();
        var currencyModel = $('#account-currency-' + accountId).text();
        var amount = $('#account-amount-' + accountId).text();

        $('#name').val(name);
        $('#accountType').val(accountType);
        $('#currencyModel').val(currencyModel);
        $('#amount').val(amount);
        $("#accountForm").attr("action", "/account/submit" + "?accountId=" + (accountId ? accountId : ""));
    });

    $("#transactionModal").on('show.bs.modal', function(e) {
        var transactionId = $(e.relatedTarget).data('transaction-id');
        if(typeof transactionId === "undefined")
            transactionId = null;

        var dateTime = $('#transaction-dateTime-' + transactionId).text();
        var amount = $('#transaction-amount-' + transactionId).text();
        var account = $('#transaction-account-' + transactionId).text();
        var category = $('#transaction-category-' + transactionId).text();
        var transactionType = $('#transaction-transactionType-' + transactionId).text();

        $('#dateTime').val(dateTime);
        $('#amount').val(amount);
        $('#account').val(account);
        $('#category').val(category);
        $('#transactionType').val(transactionType);
        $("#transactionForm").attr("action", "/transaction/submit" + "?transactionId=" + (transactionId ? transactionId : ""));
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

function dateOnChange() {
    var fromDate = document.getElementsByClassName("fromDate")[0].value;
    var upToDate = document.getElementsByClassName("upToDate")[0].value;
    top.location="/transaction/listByPeriod?" + "fromDate=" + fromDate + "&" + "upToDate=" + upToDate;
}