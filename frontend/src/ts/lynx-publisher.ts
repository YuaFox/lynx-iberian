import {html, LitElement} from 'lit'
import {customElement, property} from 'lit/decorators.js'

@customElement('lynx-publisher')
export class LynxPublisher extends LitElement {

    @property()
    name = 'demo'
    ready = false

    createRenderRoot() {
        return this
    }

    render() {
        return html`
            <div class="card ${this.ready ? `border-success` : `border-danger`} mb-3">
                <div class="card-body">
                    <div class="card-title d-flex">
                        <h4 class="me-auto mb-0 ${this.ready ? `text-success` : `text-danger`}">${this.name}</h4>
                    </div>
                    <p class="card-text">${this.ready ? `Ready` : `Not ready (Needs extra setup!)`}</p>
                </div>
            </div>
        `;
    }
}

