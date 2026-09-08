"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.SmsRetrieverWeb = void 0;
const core_1 = require("@capacitor/core");
class SmsRetrieverWeb extends core_1.WebPlugin {
    async startWatch() {
        throw this.unimplemented('SMS Retriever no está disponible en la plataforma Web.');
    }
    async getAppHash() {
        throw this.unimplemented('App Hash no está disponible en la plataforma Web.');
    }
}
exports.SmsRetrieverWeb = SmsRetrieverWeb;
//# sourceMappingURL=web.js.map