var api = require('./neo4jApi');
//var angular = require('angular');
require('jquery-match-height');
require('lazysizes');

var listWithProp;

var app = angular.module('myApp',[]);
app.controller('dbController', ['$scope', '$http', function($scope){
    $scope.listDB = [
      {value : "M", name : "Mathilde's database"},
      {value : "P", name : "Pieter's database"},
      {value : "L", name : "local database"}];
    $scope.db = function () {
      var val = $scope.selectedDB;
      if(val!=null&&val!="myOwn"){
        //getDB(val);
        //console.log("val "+val);
        proposition(val);
      }
    };
}]);
app.controller('geomController', ['$scope', '$http', function($scope){
    $scope.dikten = [
      {value : "a", num : "0-0.5"},
      {value : "b", num : "0.5-1"},
      {value : "c", num : "1-5"},
      {value : "d", num : "5-10"},
      {value : "e", num : "10-20"},
      {value : "f", num : "20-50"}];
    $scope.dikte = function () {
          //console.log("click");
          var val = $scope.selectedDikte;
          //console.log("val "+val+" "+val[0]+" "+val[1]);
          var values;
          //val = "(blanco)";
          if(val!=null){
          //haveProp("X Plus", "dikte", val);
          if (val == "a"){
            values = [0, 0.5];
          } else if (val == "b"){
            values = [0.5, 1];
          } else if (val == "c"){
            values = [1, 5];
          } else if (val == "d"){
            values = [5, 10];
          } else if (val == "e"){
            values = [10, 20];
          } else if (val == "f"){
            values = [20, 50];
          }
          var prop = "dikte";
          getList(prop, values);
        }
    };
    $scope.lengten = [
      {value : "a", num : "0-500"},
      {value : "b", num : "500-1 000"},
      {value : "c", num : "1 000-1 500"},
      {value : "d", num : "1 500-2 000"},
      {value : "e", num : "2 000-3 000"},
      {value : "f", num : "3 000-5 000"},
      {value : "g", num : "5 000-90 000"}];
    $scope.lengte = function () {
          //console.log("click");
          var val = $scope.selectedLengte;
          var values;
          //val = "(blanco)";
          if(val!=null){
          console.log("val "+val);
          //haveProp("X Plus", "dikte", val);
          if (val == "a"){
            values = [0, 500];
          } else if (val == "b"){
            values = [500, 1000];
          } else if (val == "c"){
            values = [1000, 1500];
          } else if (val == "d"){
            values = [1500, 2000];
          } else if (val == "e"){
            values = [2000, 3000];
          } else if (val == "f"){
            values = [3000, 5000];
          } else if (val == "g"){
            values = [5000, 90000];
          }
          var prop = "lengte";
          console.log(values);
          getList(prop, values);
        }
    };
    $scope.breedten = [
      {value : "a", num : "0-100"},
      {value : "b", num : "100-500"},
      {value : "c", num : "500-750"},
      {value : "d", num : "750-1000"},
      {value : "e", num : "1000-1500"}];
    $scope.breedte = function () {
          //console.log("click");
          var val = $scope.selectedBreedte;
          var values;
          //val = "(blanco)";
          if(val!=null){
          console.log("val "+val);
          //haveProp("X Plus", "dikte", val);
          if (val == "a"){
            values = [0, 100];
          } else if (val == "b"){
            values = [100, 500];
          } else if (val == "c"){
            values = [500, 750];
          } else if (val == "d"){
            values = [750, 1000];
          } else if (val == "e"){
            values = [1000, 1500];
          }
          var prop = "breedte";
          console.log(values);
          getList(prop, values);
        }
    };
}]);
//geomController.$inject = ['$scope'];

$(function () {
  //search();

  $("#search").submit(e => {
    e.preventDefault();
    search();
  });
});

// this function gives the list of the products  having
// the value in between the two values of val for the property prop
function getList(prop, val) {
  if(val!=null){
    listWithProp = [];
    //console.log("in "+prop+" "+val[0]+val[1]);
    api
      .getList(prop, val)
      .then(products => {
        if (products){
          // console.log("search");
          products.forEach(product => {
            //console.log(product.artikelNummer);
            listWithProp.push(product.artikelNummer);
          });
          console.log("list "+listWithProp);
          //$(document).ready(function(){
            //$("#Dikte").on("keyup", function() {
            //console.log($(this).text());
              $("#results *").filter(function() {
                  //console.log("show res "+$(this).find('p').text());
                  if(listWithProp.includes($(this).find('p').text())){
                    //console.log($(this).parentElement.text());
                    //console.log("res "+$(this).text()+" "+$(this).toArray());
                  }
                  $(this).parentsUntil(".results").toggle(listWithProp.includes($(this).text()));
                  //$(this).children().toggle(listWithProp.includes($(this).find('p').text()));
              });
              $("#propositions *").filter(function() {
                  if(listWithProp.includes($(this).find('p').text())){
                    //$(this).find('p').toggle(true);
                    //console.log("next "+$(this).next().next().text());
                  }
                  $(this).parentsUntil(".dropdown-menu").toggle(listWithProp.includes($(this).text()));
                  //$(this).children().toggle(listWithProp.includes($(this).find('p').text()));
              });
            //});
          //});

        }
        //console.log(dikten);
    });
    // console.log("full list "+listWithProp);
    // $("#results *").filter(function() {
    //   $(this).toggle(listWithProp.includes($(this).text()))
    // });
    // $("#propositions *").filter(function() {
    //   $(this).toggle(listWithProp.includes($(this).text()))
    // });
    //console.log(listWithProp.length);
    //console.log(name+" "+prop +" "+ val);
    // if (dikten.includes(name)){
    //   console.log("ok");
    //   return true
    // }
    // return false
  } else {
    console.log("else");
    $("#results *").filter(function() {
        $(this).toggle(true);
    });
    $("#propositions *").filter(function() {
        $(this).toggle(true);
    });
  }
}

// display product details in PRODUCT IDENTIFIERS
function showProduct(name) {

  api
    .getProduct(name)
    .then(product => {
      if (!product) return;



      $("#myinput").text(product.naamNL);
      // global information on the product
      $("#prodName").text(product.naamNL);
      $("#prodRef").text(product.artikelNummer);
      $("#prodType").text(product.typeLangNL);
      $("#prodLabel").text(product.productnorm);
      $("#benamingPrijslijstNL").text(product.benamingPrijslijstNL);

      // picture of the product
      $("#picture").attr("src", product.Image);

      // description tab
      var $list = $("#prodDescription").empty();
      $list.append($("<div><h6>Omschrijving</h6>"+product.omschrijvingNL+"</div></br>"));
      $list.append($("<div><h6>Samenstelling</h6>"+product.samenstellingNL+"</div></br>"));
      $list.append($("<div><h6>Afmeting</h6> \
      <table class=\"table\"><thead><tr><th>Type</th><th>Waarde</th></tr></thead><tbody> \
      <tr><td>dikte</td><td>"+product.dikte+"</td></tr> \
      <tr><td>totaleDikte</td><td>"+product.totaleDikte+"</td></tr> \
      <tr><td>oppervlaktegewicht</td><td>"+product.oppervlaktegewicht+"</td></tr> \
      <tr><td>basishoeveelheidseenheid</td><td>"+product.basishoeveelheidseenheid+"</td></tr> \
      <tr><td>breedte</td><td>"+product.breedte+"</td></tr> \
      <tr><td>lengte</td><td>"+product.lengte+"</td></tr> \
      <tr><td>dikteIsolatie</td><td>"+product.dikteIsolatie+"</td></tr> \
      <tr><td>kantvorm</td><td>"+product.kantvorm+"</td></tr> \
      </tbody></table></div></br>"));

      //<tr><td>benamingPrijslijstNL</td><td>"+product.benamingPrijslijstNL+"</td></tr> \
      $list.append($("<div><h6>Anders</h6> \
      <table class=\"table\"><thead><tr><th>Naam</th><th>Waarde</th></tr></thead><tbody> \
      <tr><td>typeKort</td><td>"+product.typeKort+"</td></tr> \
      <tr><td>intrastatNummer</td><td>"+product.intrastatNummer+"</td></tr> \
      <tr><td>DOPNummer</td><td>"+product.DOPNummer+"</td></tr> \
      <tr><td>status</td><td>"+product.status+"</td></tr> \
      </tbody></table></div></br>"));
      //<tr><td>oudArtikelNummer</td><td>"+product.oudArtikelNummer+"</td></tr> \

      // properties tab
      var tProp = $("table#properties tbody").empty();
      // for prop in properties
      $("<tr><td class='property'>lineaireUitzettingsCoefficientTgvRV</td><td>" + product.lineaireUitzettingsCoefficientTgvRV + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>lineaireUitzettingsCoefficientTgvTemp</td><td>" + product.lineaireUitzettingsCoefficientTgvTemp + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>waterdampdiffusieWeerstandsgetalMu</td><td>" + product.waterdampdiffusieWeerstandsgetalMu + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>thermischeGeleidbaarheidl</td><td>" + product.thermischeGeleidbaarheidl + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>lineaireUitzettingTempVWNL</td><td>" + product.lineaireUitzettingTempVWNL + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>lineaireUitzettingVochtVWFR</td><td>" + product.lineaireUitzettingVochtVWFR + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>waterdampdiffusie WeerstandsgetalIsolatieMu</td><td>" + product.waterdampdiffusieWeerstandsgetalIsolatieMu + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>lineaireUitzettingVochtVWNL</td><td>" + product.lineaireUitzettingVochtVWNL + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>thermischeGeleidbaarheidl</td><td>" + product.thermischeGeleidbaarheidl + "</td><td> </td></tr>").appendTo(tProp);
      $("<tr><td class='property'>thermischeGeleidbaarheidIsolatiel</td><td>" + product.thermischeGeleidbaarheidIsolatiel + "</td><td> </td></tr>").appendTo(tProp);

      // download tab
      var tDown = $("table#downloads tbody").empty();
      // for down in downloads if it changes a lot
      $("<tr><td class='dowload'>DOPURLFR</td><td> <a href=\"" + product.DOPURLFR + "\">"+ product.DOPURLFR +"</td></tr>").appendTo(tDown);
      $("<tr><td class='dowload'>PIBURLFR</td><td> <a href=\"" + product.PIBURLFR + "\">"+ product.PIBURLFR  + "</td></tr>").appendTo(tDown);
      $("<tr><td class='dowload'>DOPURLNL</td><td> <a href=\"" + product.DOPURLNL + "\">"+ product.DOPURLNL  + "</td></tr>").appendTo(tDown);
      $("<tr><td class='dowload'>PIBURLNL</td><td> <a href=\"" + product.PIBURLNL + "\">"+ product.PIBURLNL  + "</td></tr>").appendTo(tDown);

      // classe tab
      var tClas = $("table#classe tbody").empty();
      // for norm in norms
      $("<tr><td class='classe'>reactieBijBrandNorm</td><td>" + product.reactieBijBrandNorm + "</td></tr>").appendTo(tClas);
      $("<tr><td class='classe'>EAN</td><td>" + product.EAN + "</td></tr>").appendTo(tClas);
      //$("<tr><td class='classe'>EANEenheid</td><td>" + product.EANEenheid + "</td></tr>").appendTo(tClas);
      $("<tr><td class='classe'>reactieBijBrand</td><td>" + product.reactieBijBrand + "</td></tr>").appendTo(tClas);

    }, "json");

  api
    .getCompany(name)
    .then(company => {
      if (!company) return;

      $("#prodFabr").text(company.name);
      $("#compName").text(company.name);
      $("#compAddress").text(company.address);
      $("#phone").text(company.phone);
      $("#mail").text(company.mail);
      $("#website").attr("href", company.website);
      //$("#prodName").text(company.name);
      $("#logo").attr("src", company.logo);
    }, "json");
}

function search() {
  var query = $("#search").find("input[name=search]").val();
  api
    .searchProducts(query)
    .then(products => {
        var first = products[0];
        if (first) {
          showProduct(first.naamNL);
        }
    });
}

// display all products from the database dbname in BESCHIKBARE PRODUCTEN
function proposition(dbname) {
  api
    .searchProducts("",dbname)
    .then(products => {
      var t = $("#results").empty();
      var listprop = $("#propositions").empty();
      var count = 2;    // initialized at 2 for item index for matchHeight

      if (products) {
        products.forEach(product => {
           if(count<=12000){
              var name = product.naamNL;
              if (name!=""){
                  $("<div class=\"col-xs-6 col-sm-4 col-md-3 item item-"+count+"\"><div class=\"card\" lazy-module=\"load.html\" lazy-if=\"ctrl.acceleratePageLoad\"> \
                      <img class=\"img-thumbnail card-img-top mx-auto d-block lazyload\" style=\"max-height: 150px\"data-src=\""+product.Image+"\">\
                      <div class=\"card-body\"> \
                        <a href=\"#section1\" class=\"btn btn-block text-dark\">"+product.naamNL.substring(0,15)+"</a> \
                        <p class=\"card-text\" id=\"rProduct\" style=\"text-align:center; color:grey;\">"+product.artikelNummer+"</p> \
                      </div></div></div>").appendTo(t)
                    .click(function() {
                      showProduct(product.artikelNummer);
                    });
            count++;
          $("<a class=\"dropdown-item button\" href=\"#section1\"><div>"+product.naamNL+"<p><small id=\"pProduct\" style=\"color:grey;\">"+product.artikelNummer+"</small></p></div></a>").appendTo(listprop)
            .click(function() {
              showProduct(product.artikelNummer);
          })}}
      });
            //$('#count').text(count);

        var first = products[0];
        if (first) {
          showProduct(first.artikelNummer);
        }
      }
    });
}
