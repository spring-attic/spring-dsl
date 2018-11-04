//https://github.com/angular/angular-cli/issues/1548
const fs = require('fs');
const f = 'node_modules/@angular-devkit/build-angular/src/angular-cli-files/models/webpack-configs/browser.js';

fs.readFile(f, 'utf8', function (err,data) {
  if (err) {
    return console.log(err);
  }
  var result = data.replace(/node: false,/g, 'node: {crypto: true, stream: true, fs: \'empty\', net: \'empty\', clearImmediate: true, setImmediate: true}, resolve: {alias: {\'vscode\': require.resolve(\'monaco-languageclient/lib/vscode-compatibility\')}}');

  fs.writeFile(f, result, 'utf8', function (err) {
    if (err) return console.log(err);
  });
});
