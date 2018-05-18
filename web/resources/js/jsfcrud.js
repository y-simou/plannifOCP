function handleSubmit(args, dialog) {
    var jqDialog = jQuery('#' + dialog);
    if (args.validationFailed) {
        jqDialog.effect('shake', {times: 3}, 100);
    } else {
        PF(dialog).hide();
    }
}

function showStuff(idc, idh) {
    document.getElementById(idc).style.display = 'block';
    // hide the lorem ipsum text
    document.getElementById(idh).style.display = 'none';
}
