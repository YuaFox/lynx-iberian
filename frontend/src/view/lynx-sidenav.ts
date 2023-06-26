import {html, LitElement, css} from 'lit'
import {customElement, property} from 'lit/decorators.js'

import { LynxView } from './lynx-view'
import { LynxContainerMain } from './lynx-container-main'

@customElement('lynx-sidenav')
export class LynxSidenav extends LitElement {

    static styles = css`
        :root {
            background: #222 !important;
        }
    `;

    static elements : LynxView[] = []

    @property()
    currentView : LynxView | null = null

    createRenderRoot() {
        return this
    }

    _setView(v: LynxView) {
        this.currentView = v;
        (document.querySelector("lynx-container-main") as LynxContainerMain).view = v
    }

    render() {
        return html`
            <h4 class="text-white px-2">Lynx-Iberian</h4>
            <p class="px-2 mb-3">2023.07</p>
            ${LynxSidenav.elements.map((view: any) => html`
                <a href="#" class="d-flex h5 p-3 m-0 text-light text-decoration-none" style="${this.currentView?.name == view.name ? `background-color: rgba(var(--bs-dark-rgb),1)!important; border-radius: 5px 0 0 5px;`:``}" @click=${() => this._setView(view)}>${view.name}</a>
            `)}
        `;
    }
}

