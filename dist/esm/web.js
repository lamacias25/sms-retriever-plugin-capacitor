import { WebPlugin } from '@capacitor/core';
export class SmsRetrieverWeb extends WebPlugin {
    async startWatch() {
        throw this.unimplemented('SMS Retriever no está disponible en la plataforma Web.');
    }
    async getAppHash() {
        throw this.unimplemented('App Hash no está disponible en la plataforma Web.');
    }
}
//# sourceMappingURL=web.js.map