/* eslint-disable global-require */
import React from "react";

const Instructions = () => (
    <div>
        <h4>Step 1</h4>
        <p>Copy the URL so that you can use it later to configure the integration correctly.</p>
        <h4>Step 2</h4>
        <p>
            In your Bitbucket repository. Choose Settings in the right navigation.
        </p>
        <h4>Step 3</h4>
        <p>In the left navigation, select Webhooks, and then click select web sender.</p>
        <h4>Step 4</h4>
        <p>Paste the URL you copied earlier under Payload URL.</p>
        <p>Check the box next to Active to turn the webhook on.</p>
    </div>
);

export default Instructions;
