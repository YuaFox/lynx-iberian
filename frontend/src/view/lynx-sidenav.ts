import {html, LitElement} from 'lit'
import {customElement} from 'lit/decorators.js'

import { LynxView } from './lynx-view'
import { LynxContainerMain } from './lynx-container-main'

@customElement('lynx-sidenav')
export class LynxSidenav extends LitElement {

    static elements : LynxView[] = []


    createRenderRoot() {
        return this
    }

    _setView(v: LynxView) {
        (document.querySelector("lynx-container-main") as LynxContainerMain).view = v
    }

    render() {
        return html`
            ${LynxSidenav.elements.map((view: any) => html`
                <a href="#" class="d-flex h5 p-2 text-light text-decoration-none" ${(document.querySelector("lynx-container-main") as LynxContainerMain).view?.name == view.name ? `background-color: #111; border-radius: 5px;`:``} @click=${() => this._setView(view)}>${view.name}</a>
            `)}
        `;
    }
}

