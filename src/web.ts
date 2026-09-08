import { WebPlugin } from '@capacitor/core';
import type { SmsRetrieverPlugin } from './definitions';

export class SmsRetrieverWeb extends WebPlugin implements SmsRetrieverPlugin {
  async startWatch(): Promise<{ message: string }> {
    throw this.unimplemented('SMS Retriever no está disponible en la plataforma Web.');
  }

  async getAppHash(): Promise<{ hash: string }> {
    throw this.unimplemented('App Hash no está disponible en la plataforma Web.');
  }
}