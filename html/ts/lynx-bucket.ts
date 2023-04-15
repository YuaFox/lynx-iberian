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
            <div class="card border-primary mb-3">
                <div class="card-body">
                    <div class="card-title d-flex">
                        <h4 class="me-auto mb-0">${this.name}</h4>
                        <button type="button" class="btn-close" @click="${this._delete}"></button>
                    </div>
                    <p class="card-text">Local bucket</p>
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

