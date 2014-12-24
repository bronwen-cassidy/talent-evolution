/*
 author taulant bajraktari
 */

/**
 * spell check using spellchecker dwr bean
 * @param elem - word to spell#
 * @return a xml feed back - i.e. google xml reply
 */
function spellcheck(elem) {
    //
    elem.onblur = function() {

        googleSpellCheckerBean.getSpelledWords(elem.value,
        { callback:function(result) {

            alert("result is " + result);

        },async:false
        });
    }
}

function assignGoogleSpellElement(elem) {

    if ((elem.type == "text") || (elem.type == "textarea")) {
        if (!elem.readOnly) {
            assignGoogleSpellRegister(elem);
        }
    }
}
/**
 * register an element to have spell checker assigned to it
 * using googie api
 * @param elem
 */
function assignGoogleSpellRegister(elem) {

    var el = getElemById("spellcheckerUrl");
    var img = getElemById("spellcheckerImageUrl");
    //todo most likely this is issue for multiple text fields - see demo for googie api to transfer to
    var googie5 = new GoogieSpell(img.value, el.value);
    googie5.setSpellContainer("spell_container");
    googie5.decorateTextarea(elem, AJS.$(elem));
}
/**
 * assign google spell check to all forms
 */
function assignGoogleSpellToAllForms() {

    var allowElem = getElemById("google_spell_allow");
    if (allowElem) {
        assignGoogleSpellCheckToAllForms();
    }
}

function assignGoogleSpellCheckToAllForms() {
    var allTextAreaElems = document.getElementsByTagName('textarea');
    //var allTextElems = document.getElementsByTagName('input');
    assignGogoleSpellCheckToElements(allTextAreaElems);
    //assignGogoleSpellCheckToElements(allTextElems);


}
function assignGogoleSpellCheckToElements(allTextAreaElems) {
    try {
        if (allTextAreaElems.length > 0)
        {
            for (var i = 0; i < allTextAreaElems.length; i++) {
                var textAreaElem = allTextAreaElems[i];
                //if ((textAreaElem.type == "text") || (textAreaElem.type == "textarea")) {
                if (textAreaElem.type == "textarea") {
                    if (!textAreaElem.readOnly && !textAreaElem.disabled) {
                        assignGoogleSpellRegister(textAreaElem);
                    }
                }

            }
        }
    }
    catch(e) {
        alert("error " + e);
    }
}

