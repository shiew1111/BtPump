import { WebPlugin } from '@capacitor/core';
export class BtPumpPluginWeb extends WebPlugin {
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
export { BtPumpPlugin };
import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BtPumpPlugin);
//# sourceMappingURL=web.js.map