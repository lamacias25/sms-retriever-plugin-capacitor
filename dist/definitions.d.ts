export interface SmsRetrieverPlugin {
    startWatch(): Promise<{
        message: string;
    }>;
    getAppHash(): Promise<{
        hash: string;
    }>;
}
//# sourceMappingURL=definitions.d.ts.map