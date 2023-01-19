import { DocsThemeConfig, useConfig } from "nextra-theme-docs";
import { useRouter } from "next/router";
import { Logo } from "@components/Logo";

const TITLE_WITH_TRANSLATION = {
  "en-US": "Action RPG drop in Spigot",
};

const config: DocsThemeConfig = {
  logo: () => {
    const { locale } = useRouter();
    return (
      <>
        <Logo height={12} />
        <span
          className="mx-2 font-extrabold hidden md:inline select-none"
          title={`MythicDrops: ${TITLE_WITH_TRANSLATION[locale] ?? ""}`}
        >
          MythicDrops
        </span>
      </>
    );
  },
  head: () => {
    const { frontMatter } = useConfig();
    return (
      <>
        <link rel="icon" type="image/svg+xml" href="/favicon.svg" />
        <link rel="alternate icon" type="image/png" href="/favicon.ico" />
        <meta name="msapplication-TileColor" content="#ffffff" />
        <meta httpEquiv="Content-Language" content="en" />
        <meta
          name="description"
          content={
            frontMatter.description ||
            "MythicDrops is a Spigot plugin that enables Action RPG drops in Minecraft."
          }
        />
        <meta
          name="og:description"
          content={
            frontMatter.description ||
            "MythicDrops is a Spigot plugin that enables Action RPG drops in Minecraft."
          }
        />
        <meta
          name="og:title"
          content={
            frontMatter.title
              ? frontMatter.title + " – MythicDrops"
              : "MythicDrops: Action RPG drops in Spigot"
          }
        />
      </>
    );
  },
  editLink: {
    text: () => {
      const { locale } = useRouter();
      switch (locale) {
        default:
          return <>Edit this page on GitHub →</>;
      }
    },
  },
  feedback: {
    content: () => {
      const { locale } = useRouter();
      switch (locale) {
        default:
          return <>Question? Give us feedback →</>;
      }
    },
  },
  project: {
    link: "https://github.com/MythicDrops/MythicDrops",
  },
  chat: {
    link: "https://discord.gg/ANu94t2B9C",
  },
  docsRepositoryBase: "https://github.com/MythicDrops/MythicDrops/tree/main/docs",
  footer: {
    text: () => {
      const { locale } = useRouter();
      switch (locale) {
        default:
          return (
            <>MIT 2013-PRESENT © Richard Harrah and MythicDrops contributors.</>
          );
      }
    },
  },
  i18n: [{ locale: "en-US", text: "English" }],
};

export default config;
