import { join } from "path";
import propertiesReader from "properties-reader";
import { defineConfig } from "vitepress";
import {
  discord,
  mythicDropsDescription,
  mythicDropsName,
  releases,
} from "../docs-data";

const version = propertiesReader(
  join(__dirname, "..", "..", "version.properties")
).get("version");

export default defineConfig({
  title: mythicDropsName,
  description: mythicDropsDescription,
  head: [
    ["meta", { name: "theme-color", content: "#ffffff" }],
    ["link", { rel: "icon", href: "/logo.svg", type: "image/svg+xml" }],
    [
      "link",
      {
        rel: "alternate icon",
        href: "/favicon.ico",
        type: "image/png",
        sizes: "16x16",
      },
    ],
    ["meta", { property: "og:title", content: mythicDropsName }],
    ["meta", { property: "og:description", content: mythicDropsDescription }],
    ["meta", { name: "twitter:title", content: mythicDropsName }],
    ["meta", { name: "twitter:description", content: mythicDropsDescription }],
    ["link", { rel: "mask-icon", href: "/logo.svg", color: "#ffffff" }],
  ],
  themeConfig: {
    repo: "MythicDrops/MythicDrops",
    logo: "/logo.svg",
    docsDir: "docs",
    docsBranch: "main",
    editLinks: true,
    editLinkText: "Suggest changes to this page",

    algolia: {
      appId: "NT6ELKD6SW",
      apiKey: "c83c78c670bb63e33d395bc67f382acc",
      indexName: "prod_MythicDrops",
    },

    nav: [
      { text: "Guide", link: "/guide/" },
      { text: "Config", link: "/config/" },
      {
        text: `v${version}`,
        items: [
          {
            text: "Release Notes ",
            link: releases,
          },
        ],
      },
      {
        text: "Javadocs",
        link: "https://javadoc.io/doc/io.pixeloutlaw.mythicdrops/mythicdrops",
      },
      {
        text: "Discord",
        link: discord,
      },
    ],

    sidebar: {
      "/config/": [
        {
          text: "Config",
          children: [
            { text: "Overview", link: "/config/" },
            { text: "config.yml", link: "/config/config.yml.html" },
            {
              text: "creatureSpawning.yml",
              link: "/config/creatureSpawning.yml.html",
            },
            { text: "customItems.yml", link: "/config/customItems.yml.html" },
            { text: "identifying.yml", link: "/config/identifying.yml.html" },
            { text: "itemGroups.yml", link: "/config/itemGroups.yml.html" },
            { text: "language.yml", link: "/config/language.yml.html" },
            { text: "relation.yml", link: "/config/relation.yml.html" },
            { text: "repairCosts.yml", link: "/config/repairCosts.yml.html" },
            { text: "repairing.yml", link: "/config/repairing.yml.html" },
            { text: "socketGems.yml", link: "/config/socketGems.yml.html" },
            { text: "socketing.yml", link: "/config/socketing.yml.html" },
            { text: "resources Directory", link: "/config/resources-directory.html" },
            { text: "tiers Directory", link: "/config/tiers-directory.html" },
          ],
        },
      ],
      // catch-all fallback
      "/": [
        {
          text: "Guide",
          children: [
            { text: "Getting Started", link: "/guide/" },
            { text: "Features", link: "/guide/features" },
            { text: "Commands", link: "/guide/commands" },
            { text: "Permissions", link: "/guide/permissions" },
          ],
        },
      ],
    },
  },
});
