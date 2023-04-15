import { LitElement } from 'lit';
import { LynxBucket } from './lynx-bucket';
export declare class LynxBuckets extends LitElement {
    form_new_bucket_name: string;
    buckets: never[];
    bucket_items: LynxBucket[];
    createRenderRoot(): this;
    render(): import("lit-html").TemplateResult<1>;
    _input_changed(e: any): void;
    _createBucket(): void;
    _query(): void;
    firstUpdated(): void;
}
