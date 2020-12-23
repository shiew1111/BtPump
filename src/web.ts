import { WebPlugin } from '@capacitor/core';
import { BtPumpPluginPlugin } from './definitions';

export class BtPumpPluginWeb extends WebPlugin implements BtPumpPluginPlugin {
  constructor() {
    super({
      name: 'BtPumpPlugin',
      platforms: ['web'],
    });
  }



  connect(): Promise<{ isConnected: string }> {
    return new Promise(function (resolve) {
      resolve({isConnected:"You can't connect with pump from browser!"});
    })
  }


  pairing(): Promise<{ mac: string }> {
    return new Promise(function (resolve) {
      resolve({mac:"You can't pair with pump from browser!"});
    })
  }

  GetPumpData(): Promise<{ status: string }> {
    return new Promise(function (resolve) {
      resolve({status:"You can't get pump status from browser!"});
    })
  }

  async disconnect(): Promise<void> {

  }

  keyCheck(): Promise<{ key: string }> {
    return new Promise(function (resolve) {
      resolve({key:"You can't use pump keys from browser!"});
    })  }

  keyDown(): Promise<{ key: string }> {
    return new Promise(function (resolve) {
      resolve({key:"You can't use pump keys from browser!"});
    })
  }

  keyMenu(): Promise<{ key: string }> {
    return new Promise(function (resolve) {
      resolve({key:"You can't use pump keys from browser!"});
    })
  }

  keyUp(): Promise<{ key: string }> {
    return new Promise(function (resolve) {
      resolve({key:"You can't use pump keys from browser!"});
    })
  }
}

const BtPumpPlugin = new BtPumpPluginWeb();

export { BtPumpPlugin };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(BtPumpPlugin);
