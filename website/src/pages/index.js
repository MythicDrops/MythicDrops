import React from "react";
import clsx from 'clsx';
import Layout from "@theme/Layout";
import Link from "@docusaurus/Link";
import useDocusaurusContext from "@docusaurus/useDocusaurusContext";
import useBaseUrl from "@docusaurus/useBaseUrl";
import styles from "./styles.module.css";

const features = [
  {
    title: <>Randomized Items</>,
    description: (
      <>
        With over 52 billion items able to be generated from the default
        settings, MythicDrops can power your RPG Minecraft server with any items
        you can imagine.
      </>
    )
  },
  {
    title: <>Socket Gems</>,
    description: (
      <>
        An action RPG favorite, Socket Gems can add effects to your items. Apply
        blindness on hit, confuse your enemies when hit. The possibilities are
        endless.
      </>
    )
  },
  {
    title: <>Unidentified Items</>,
    description: (
      <>
        Items can drop as Unidentified Items that need to be identified in order
        to gain value. Is the drop you just got from that Zombie a common drop
        or is it a Legendary?
      </>
    )
  }
];

function Home() {
  const context = useDocusaurusContext();
  const { siteConfig = {} } = context;
  return (
    <Layout title={siteConfig.title} description={siteConfig.tagline}>
      <div className={styles.displayFlex}>
        <header
          className={clsx(
            "hero hero--primary",
            styles.heroBanner,
            styles.flexDontGrow
          )}
        >
          <div className="container">
            <h1 className="hero__title">{siteConfig.title}</h1>
            <p className="hero__subtitle">{siteConfig.tagline}</p>
            <div className={styles.buttons}>
              <Link
                className={clsx(
                  "button button--secondary button--lg",
                  styles.getStarted
                )}
                to={useBaseUrl("docs/")}
              >
                Get Started
              </Link>
            </div>
          </div>
        </header>
        <main className={styles.flexGrow}>
          {features && features.length && (
            <section className={styles.features}>
              <div className="container">
                <div className="row">
                  {features.map(({ imageUrl, title, description }, idx) => (
                    <div
                      key={idx}
                      className={clsx("col col--4", styles.feature)}
                    >
                      {imageUrl && (
                        <div className="text--center">
                          <img
                            className={styles.featureImage}
                            src={useBaseUrl(imageUrl)}
                            alt={title}
                          />
                        </div>
                      )}
                      <h3>{title}</h3>
                      <p>{description}</p>
                    </div>
                  ))}
                </div>
              </div>
            </section>
          )}
        </main>
      </div>
    </Layout>
  );
}

export default Home;
