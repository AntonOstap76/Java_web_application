// javascript code for country page

var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

$(document).ready(function(){
        buttonLoad=$("#buttonLoadCountries");
        dropDownCountry=$("#dropDownCountries");
    buttonAddCountry=$("#buttonAddCountry");
    buttonUpdateCountry=$("#buttonUpdateCountry");
    buttonDeleteCountry=$("#buttonDeleteCountry");
    labelCountryName=$("#labelCountryName");
    fieldCountryName=$("#fieldCountryName");
    fieldCountryCode=$("#fieldCountryCode");

    buttonLoad.click(function(){
        loadCountries();
    });

    dropDownCountry.on("change", function(){
        changeFormStateToSelectedCountry();
    });

    buttonAddCountry.click(function(){
        if(buttonAddCountry.val()=="Add"){
            addCountry();
        }
        else{
            changeFormStateToNew();
        }
    });

    buttonUpdateCountry.click(function(){
        updateCountry();
    });

    buttonDeleteCountry.click(function(){
        deleteCountry();
    });

});

function deleteCountry(){
    optionValue = dropDownCountry.val();
    countryId= optionValue.split("-")[0];
    url=contextPath+"countries/delete/"+countryId;

    $.get(url, function(){
        $("#dropDownCountries option[value='"+optionValue+"']").remove();
        changeFormStateToNew();
    }).done(function(){
        showToastMessage("The country has  been deleted");
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}


function updateCountry(){
    url=contextPath+"countries/save";

    //    country data will be what we write in the input fields
    countryName=fieldCountryName.val();
    countryCode=fieldCountryCode.val();

//    getting country id for updating
    countryId=dropDownCountry.val().split("-")[0];

    jsonData={id:countryId, name:countryName, code:countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType:'application/json'
    }).done(function(countryId){
        $("#dropDownCountries option:selected").val(countryId+"-"+countryCode);
        $("#dropDownCountries option:selected").text(countryName);

        showToastMessage("The country has been updated");

        changeFormStateToNew();

    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function addCountry(){
    url=contextPath+"countries/save";

//    country data will be what we write in the input fields
    countryName=fieldCountryName.val();
    countryCode=fieldCountryCode.val();

    jsonData={name:countryName, code:countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType:'application/json'
    }).done(function(countryId){
        selectNewlyAddedCountry(countryId,countryCode, countryName);
        showToastMessage("The new country has been added");
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });


}

function selectNewlyAddedCountry(countryId,countryCode, countryName){

    optionValue = countryId+"-"+countryCode;

//    add new country to option
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);

//    add option with the same optionValue to dropdownList
    $("#dropDownCountry option[value='"+optionValue+"']").prop("selected", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();

}

function changeFormStateToNew(){
//    when select a country and click new button this function will work
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name:");

    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);

    fieldCountryName.val("").focus();
    fieldCountryCode.val("");

}

function changeFormStateToSelectedCountry(){
    buttonAddCountry.prop("value", "New");
//    disable a update button when no country selected
    buttonUpdateCountry.prop("disabled", false);
    //    disable a delete button when no country selected
    buttonDeleteCountry.prop("disabled", false);

//    when change a country country name changes to selected country
    labelCountryName.text("Selected Country:");

    selectedCountryName=$("#dropDownCountries option:selected").text();
//    input field is filled by the selected country from dropdown list
    fieldCountryName.val(selectedCountryName);

    // take only letters part of the code
    countryCode = dropDownCountry.val().split("-")[1];

// input field is filled by code from selected country
    fieldCountryCode.val(countryCode);
}


function loadCountries(){

    url = contextPath + "countries/list";
    $.get(url, function(responseJSON){
       dropDownCountry.empty();

        $.each(responseJSON, function(index, country){
            optionValue = country.id + "-" + country.code;
            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        });

    }).done(function(){
        buttonLoad.val("Refresh Country List");
        showToastMessage("All countries have been loaded");
    }).fail(function(){
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function showToastMessage(message){

    $("#toastMessage").text(message);
    $(".toast").toast("show");

}