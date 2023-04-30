import {html, css, LitElement} from 'lit';
import {customElement, property} from 'lit/decorators.js';

import axios from 'axios'

@customElement('lynx-bucket')
export class LynxBucket extends LitElement {

    static styles = css`p { color: blue }`;

    @property()
    name = 'demo';

    createRenderRoot() {
        return this;
    }

    render() {
        return html`
            <div class="flex-fill row m-0">
                <h4 class="col-md-4 text-white me-auto">
                    ${this.name}
                </h4>
                <p class="col-md-7 text-white">Local bucket</p>
                <div class="col-md-1">
                    <button type="button" class="text-white h4" style="background: none;border: none;" @click="${this._delete}">
                        <i class="fa-solid fa-xmark"></i>
                    </button>
                </div>
            </div>
        `;
    }

    _delete() {
        if(confirm(`Delete bucket ${this.name}?`)){
            axios.delete(`/api/v1/bucket/${this.name}`).then(function () {
                alert(`Bucket ${name} deleted.`)
            })
        }
    }
}

