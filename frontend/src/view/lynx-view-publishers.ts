import {html} from 'lit'
import {customElement, property} from 'lit/decorators.js'
import { LynxPublisher } from '../ts/lynx-publisher'

import axios from 'axios'

import { LynxView } from './lynx-view'


@customElement('lynx-view-publishers')
export class LynxViewPublishers extends LynxView {

    @property()
    publishers : LynxPublisher[] = []

    constructor() {
        super("Publishers")
    }

    createRenderRoot() {
        return this
    }

    render() {
        return html`
            <h2 class="my-4">Publishers</h2>
            <div>
                ${this.publishers.map((publisher: LynxPublisher) => publisher)}
            </div>
        `
    }

    firstUpdated() {
        const self = this
        axios.get("/api/v1/drivers/publisher").then((response) => {
            self.publishers = []

            response.data.map((publisher: any) => {
                let e = new LynxPublisher()
                e.name = publisher.name
                e.ready = publisher.ready
                self.publishers.push(e)
            })

            self.publishers.sort((a, b) => a.ready ? -1 : b.ready ? 1 : 0)
        })
    }
}

