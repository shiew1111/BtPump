var capacitorPlugin = (function (exports, core) {
    'use strict';

    class BtPumpPluginWeb extends core.WebPlugin {
        constructor() {
            super({
                name: 'BtPumpPlugin',
                platforms: ['web'],
            });
        }
        connect() {
            return new Promise(function (resolve) {
                resolve({ isConnected: "You can't connect with pump from browser!" });
            });
        }
        pairing() {
            return new Promise(function (resolve) {
                resolve({ mac: "You can't pair with pump from browser!" });
            });
        }
        GetPumpData() {
            return new Promise(function (resolve) {
                resolve({ status: "You can't get pump status from browser!" });
            });
        }
        disconnect() {
            return new Promise(function (resolve) {
                resolve({ isConnected: "You can't connect with pump from browser!" });
            });
        }
        keyCheck() {
            return new Promise(function (resolve) {
                resolve({ key: "You can't use pump keys from browser!" });
            });
        }
        keyDown() {
            return new Promise(function (resolve) {
                resolve({ key: "You can't use pump keys from browser!" });
            });
        }
        keyMenu() {
            return new Promise(function (resolve) {
                resolve({ key: "You can't use pump keys from browser!" });
            });
        }
        keyUp() {
            return new Promise(function (resolve) {
                resolve({ key: "You can't use pump keys from browser!" });
            });
        }
    }
    const BtPumpPlugin = new BtPumpPluginWeb();
    core.registerWebPlugin(BtPumpPlugin);

    exports.BtPumpPlugin = BtPumpPlugin;
    exports.BtPumpPluginWeb = BtPumpPluginWeb;

    Object.defineProperty(exports, '__esModule', { value: true });

    return exports;

}({}, capacitorExports));
//# sourceMappingURL=plugin.js.map
