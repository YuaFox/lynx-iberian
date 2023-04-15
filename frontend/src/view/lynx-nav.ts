import {html, LitElement} from 'lit'
import {customElement} from 'lit/decorators.js'
import { LynxView } from './lynx-view'

@customElement('lynx-nav')
export class LynxNav extends LitElement {

    elements : LynxView[] = [];

    createRenderRoot() {
        return this
    }

    render() {
        return html`<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container-fluid">
          <a class="navbar-brand" href="#">Lynx-Iberian</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor01" aria-controls="navbarColor01" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarColor01">
            <ul class="navbar-nav me-auto">
            </ul>
          </div>
        </div>
      </nav>`;
    }
}

