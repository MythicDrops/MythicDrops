import React from 'react';
import Layout from '@theme/Layout';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';

function Support() {
    const context = useDocusaurusContext();
    const {siteConfig = {}} = context;
    return (
        <Layout title={siteConfig.title} description={siteConfig.description}>
            <div className="container">
                <div className="row">
                    <div className="col col--6 col--offset-3 padding-vert--lg">
                        <h1>Need Help?</h1>
                        <p>MythicDrops is worked on by one developer in his spare time. He checks Discord regularly and
                            responds fairly quickly.</p>
                        <h2>Discord</h2>
                        <p>Many users of the MythicDrops plugin use Discord to receive updates and get help with the
                            plugin. <a href="https://discord.gg/35BKfCB">Join the Discord</a> for quick responses and
                            interactive help from the developer and/or the community.</p>
                        <h2>GitHub Issues</h2>
                        <p><a href="https://github.com/PixelOutlaw/MythicDrops/issues">You can create a GitHub
                            issue</a> and the developer will review it and begin the triage process.</p>
                    </div>
                </div>
            </div>
        </Layout>
    )
}

export default Support;
