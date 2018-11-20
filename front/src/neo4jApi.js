require('file?name=[name].[ext]!../../node_modules/neo4j-driver/lib/browser/neo4j-web.min.js');
var Product = require('./models/Product');
var Company = require('./models/Company');
var _ = require('lodash');

/* --------- neo4j javascript driver ---------- */
//var neo4j = window.neo4j.v1;
//var driver;

var neo4j = require('neo4j-driver').v1;
var driver = neo4j.driver("bolt://hobby-gmpfaohcoeaggbkeodibkpol.dbs.graphenedb.com:24786", neo4j.auth.basic("mathilde", "b.pzdiJY3X2OFl.cx1NC0H3VLOJcAe2"), {encrypted: true});
/* -------- local database -------- */
// var driver = neo4j.driver("bolt://localhost", neo4j.auth.basic("neo4j", "password"));
/* -------- online database -------- */
// you can change this variable to access your database

var DATABASE_URL = '';
var USERNAME = '';
var PASSWORD = '';


// looking for the product with the name
function searchProducts(queryString, name) {
  // if (name=="L"){
  //   console.log("local");
  //   DATABASE_URL = 'bolt://localhost';
  //   USERNAME = 'neo4j';
  //   PASSWORD = 'password';
  // } else if (name=="M"){
  //   console.log("mat");
  //   DATABASE_URL = 'bolt://hobby-bnnpmgoegikmgbkeheldgibl.dbs.graphenedb.com:24786';
  //   USERNAME = 'webapp';
  //   PASSWORD = 'b.1mnQoaOWk50Y.XMwt8eAzqKaeaRA9';
  // } else if (name=="P"){
  //   console.log("Pieter");
  //   DATABASE_URL = 'bolt://hobby-gmpfaohcoeaggbkeodibkpol.dbs.graphenedb.com:24786' ;
  //   USERNAME = 'mathilde';
  //   PASSWORD = 'b.pzdiJY3X2OFl.cx1NC0H3VLOJcAe2';
  // }
  //driver = neo4j.driver(DATABASE_URL, neo4j.auth.basic(USERNAME, PASSWORD), {encrypted: true});
  var session = driver.session();
  return session
    .run(
      'MATCH (product:Subject) \
      WHERE product.naamNL =~ {name} \
      RETURN product ORDER BY product.naamNL',
      {name: '(?i).*' + queryString + '.*'}
    )
    .then(result => {
      session.close();
      return result.records.map(record => {
        console.log("search :"+record.get('product'));
        return new Product(record.get('product'));
      });
    })
    .catch(error => {
      session.close();
      throw error;
    });
}


// getting the product and the company that sells it
function getProduct(number) {
  var session = driver.session();
  //console.log("getProduct "+name);
  return session
    .run(
      "MATCH (product:Subject {artikelNummer:{number}}) \
      RETURN product LIMIT 1", {number})
    .then(result => {
      session.close();

      if (_.isEmpty(result.records))
        return null;

      var record = result.records[0];
      //console.log("prod : "+record.get('product'));
      return new Product(record.get('product'));
      //return new ProductCast(record.get('name'), record.get('cast'));    // may be replaced by New Company
    })
    .catch(error => {
      session.close();
      throw error;
    });
}


function getCompany(name) {
  var session = driver.session();
  //console.log("getCompany "+name);
  return session
    .run(
      "MATCH (product:Subject {artikelNummer: {name}})<-[hasProduct]-(company:Company) \
      RETURN product.artikelNummer AS pname, company LIMIT 1", {name})
    .then(result => {
      session.close();

      if (_.isEmpty(result.records))
        return null;

      var record = result.records[0];
      //console.log("comp : "+record.get('company'));
      return new Company(record.get('company'));
      //return new ProductCast(record.get('name'), record.get('cast'));    // may be replaced by New Company
    })
    .catch(error => {
      session.close();
      throw error;
    });
}

function getList(prop, val) {
  var session = driver.session();
  //console.log("req");
  //console.log("getCompany "+name);
  // console.log("MATCH (product:Subject) \
  // WHERE ("+val[0]+"<=toFloat(product."+prop+")<="+val[1]+")\
  // RETURN product");
  return session
    .run(
      "MATCH (product:Subject) \
      WHERE (toFloat("+val[0]+")<=toFloat(product."+prop+")<=toFloat("+val[1]+"))\
      RETURN product")
    .then(result => {
      session.close();
      return result.records.map(record => {
        //console.log("search :"+record.get('product'));
        return new Product(record.get('product'));
      });

      // if (_.isEmpty(result.records)){
      //   //console.log(false);
      //   return null;}
      //
      // var record = result.records[0];
      // return new Product(record.get('product'));
      //console.log("comp : "+record.get('company'));
      // console.log(true);
      // return true
      //return new ProductCast(record.get('name'), record.get('cast'));    // may be replaced by New Company
    })
    .catch(error => {
      session.close();
      throw error;
    });
}

// function searchAllProducts(){
//   var session = driver.session();
//   //console.log("getCompany "+name);
//   return session
//     .run(
//       "MATCH (product:Subject) RETURN product")
//     .then(result => {
//       session.close();
//       return result.records.map(record => {
//         //console.log("search :"+record.get('product'));
//         return new Product(record.get('product'));
//       });
//     })
//     .catch(error => {
//       session.close();
//       throw error;
//     });
//
// }

exports.searchProducts = searchProducts;
exports.getCompany = getCompany;
exports.getProduct = getProduct;
exports.getList = getList;
// exports.getDB = getDB;
// exports.searchAllProducts = searchAllProducts;
