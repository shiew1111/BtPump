import { WebPlugin } from '@capacitor/core';
import { BtPumpPluginPlugin } from './definitions';
export declare class BtPumpPluginWeb extends WebPlugin implements BtPumpPluginPlugin {
    constructor();
    connect(): Promise<{
        isConnected: string;
    }>;
    pairing(): Promise<{
        mac: string;
    }>;
    GetPumpData(): Promise<{
        status: string;
    }>;
    disconnect(): Promise<{
        isConnected: string;
    }>;
    keyCheck(): Promise<{
        key: string;
    }>;
    keyDown(): Promise<{
        key: string;
    }>;
    keyMenu(): Promise<{
        key: string;
    }>;
    keyUp(): Promise<{
        key: string;
    }>;
}
declare const BtPumpPlugin: BtPumpPluginWeb;
export { BtPumpPlugin };
