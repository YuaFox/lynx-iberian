import {html} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import axios from 'axios';

import { LynxBucket } from '../ts/lynx-bucket';
import { LynxView } from './lynx-view'

@customElement('lynx-buckets')
export class LynxViewBuckets extends LynxView {

    @property()
    form_new_bucket_name = ""

    @property()
    buckets = []

    bucket_items : LynxBucket[] = []

    constructor() {
        super("Buckets")
    }

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            <div class="p-4 m-4" style="background-color: #111; border-radius: 5px;">
                <h4 class="me-auto mb-0 text-white">Create bucket</h4>
                <p class="text-white">A bucket is where media is stored.</p>

                <div class="row">
                    <div class="col-sm-2 text-white">Name</div>
                    <div class="col-sm-10">
                         <input type="text" class="form-control" @change=${this._input_changed}>
                    </div>
                </div>

                <button type="button" class="btn btn-light mt-1" @click=${this._createBucket}>Create</button>
            </div>

            <div class="p-4">
                ${this.bucket_items.map((bucket: any) => bucket)}
            </div>
        `;
    }

    _input_changed(e: any) {
        this.form_new_bucket_name = e?.target.value
    }

    _createBucket(){
        const self = this
        if(this.form_new_bucket_name === "")
            return alert("Name cannot be blank.")
        const name = this.form_new_bucket_name
        this.form_new_bucket_name = ""
        axios.post('/api/v1/bucket', null, {
            params: {
                name: name
            }
        }).then(function () {
            alert(`Bucket ${name} created!`)
            self._query()
        })
    }

    _query() {
        const self = this
        axios.get("/api/v1/bucket", {
            auth: {
                username: "lynx",
                password: "lynx"
            }
        }).then((response) => {
            self.buckets = response.data
            self.bucket_items = []
            this.buckets.map((bucket: any) => {
                let e = new LynxBucket()
                e.name = bucket.name
                self.bucket_items.push(e)
            })
        })
    }

    firstUpdated() {
        this._query()
    }
}

