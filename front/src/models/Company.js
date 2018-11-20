// var _ = require('lodash');
//
// function Company(pName, comp) {
//   _.extend(this, {
//     pName: pName,
//     comp: comp.map(function (c) {
//       return {
//         name: c[0]
//         //adress: c[1],
//         //tel: c[2]
//       }
//     })
//   });
// }
//
// module.exports = Company;
//
//
var _ = require('lodash');

function Company(_node) {

    _.extend(this, _node.properties);

      if (this.id) {
        this.id = this.id.toNumber();
      }
}

module.exports = Company;
//
// var _ = require('lodash');
//
// function Company(_node) {
//   _.extend(this, _node.properties);
//
//   if (this.id) {
//     this.id = this.id.toNumber();
//   }
// }
//
// module.exports = Company;
