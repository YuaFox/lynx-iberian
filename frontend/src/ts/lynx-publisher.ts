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
            <div class="flex-fill row m-0">
                <h4 class="col-md-1 ${this.ready ? `text-success` : `text-danger`}">
                    ${this.ready ? html`<i class="fa-solid fa-check"></i>` : html`<i class="fa-solid fa-xmark pe-1"></i>`}
                </h4>
                <h4 class="col-md-4 ${this.ready ? `text-success` : `text-danger`}">
                    ${this.name}
                </h4>
                <p class="col-md-7 text-white">${this.ready ? `Ready` : `Not ready (Needs extra setup!)`}</p>
            </div>
        `;
    }
}

