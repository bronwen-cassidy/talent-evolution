function setSearchStarted(formName)
{
	var form = document.forms[formName];
	form.searchStarted.value = 'true';
}

function initiateSearch(formName)
{
	var form = document.forms[formName];
	form.search_initiated.value = 'YES';
}