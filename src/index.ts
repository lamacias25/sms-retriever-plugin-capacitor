import { registerPlugin } from '@capacitor/core';
import type { SmsRetrieverPlugin } from './definitions';

const SmsRetriever = registerPlugin<SmsRetrieverPlugin>('SmsRetriever');

export * from './definitions';
export { SmsRetriever };