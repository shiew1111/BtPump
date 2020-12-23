declare module '@capacitor/core' {
    interface PluginRegistry {
        BtPumpPlugin: BtPumpPluginPlugin;
    }
}
export interface BtPumpPluginPlugin {
    pairing(options: {
        value: any;
    }): Promise<{
        mac: string;
    }>;
    connect(): Promise<{
        isConnected: string;
    }>;
    GetPumpData(): Promise<{
        status: string;
    }>;
    disconnect(): Promise<{
        isConnected: string;
    }>;
    keyUp(): Promise<{
        key: string;
    }>;
    keyDown(): Promise<{
        key: string;
    }>;
    keyMenu(): Promise<{
        key: string;
    }>;
    keyCheck(): Promise<{
        key: string;
    }>;
}
