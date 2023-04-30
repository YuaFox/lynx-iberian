import {html, LitElement} from 'lit'
import {customElement, property} from 'lit/decorators.js'

import { LynxView } from './lynx-view'

@customElement('lynx-container-main')
export class LynxContainerMain extends LitElement {

    @property()
    view? : LynxView = undefined

    createRenderRoot() {
        return this
    }

    render() {
        return html`${this.view}`
    }
}

