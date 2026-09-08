import { WebPlugin } from '@capacitor/core';
import type { SmsRetrieverPlugin } from './definitions';
export declare class SmsRetrieverWeb extends WebPlugin implements SmsRetrieverPlugin {
    startWatch(): Promise<{
        message: string;
    }>;
    getAppHash(): Promise<{
        hash: string;
    }>;
}
//# sourceMappingURL=web.d.ts.map