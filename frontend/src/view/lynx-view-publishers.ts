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
            <div class="p-4 m-4" style="background-color: #111; border-radius: 5px;">
                <p class="text-white h4">Send a post now!</p>
                <button type="button" class="btn btn-light mt-1" @click=${this._firePostEvent}>
                    <i class="fa-solid fa-paper-plane me-1"></i>
                    Publish
                </button>
            </div>
            <div class="p-4">
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

    _firePostEvent(){
        axios.post("/api/v1/event", {
            "@class": "dev.yua.lynxiberian.events.definitions.TimeEvent",
            "timeName": "post"
        }).then(() => {
            alert("Post sent")
        })
    }
}

