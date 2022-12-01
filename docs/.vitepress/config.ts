import { defineConfig } from "vitepress";
import {
  contributing,
  discord,
  github,
  mythicDropsDescription,
  mythicDropsName,
  releases,
} from "./meta";
import { teamMembers } from "./contributors";

export default defineConfig({
  lang: "en-US",
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
    [
      "meta",
      {
        name: "author",
        content: `${teamMembers
          .map((c) => c.name)
          .join(", ")} and ${mythicDropsName} contributors`,
      },
    ],
    [
      "meta",
      {
        name: "keywords",
        content:
          "mythic, drops, mythicdrops, minecraft, rpg, action rpg, arpg, spigot",
      },
    ],
    ["meta", { property: "og:title", content: mythicDropsName }],
    ["meta", { property: "og:description", content: mythicDropsDescription }],
    ["meta", { name: "twitter:title", content: mythicDropsName }],
    ["meta", { name: "twitter:description", content: mythicDropsDescription }],
    ["link", { rel: "mask-icon", href: "/logo.svg", color: "#ffffff" }],
  ],
  lastUpdated: true,
  markdown: {
    theme: {
      light: "vitesse-light",
      dark: "vitesse-dark",
    },
  },
  themeConfig: {
    logo: "/logo.svg",

    editLink: {
      pattern:
        "https://github.com/MythicDrops/MythicDrops/edit/main/docs/:path",
      text: "Suggest changes to this page",
    },

    algolia: {
      appId: "NT6ELKD6SW",
      apiKey: "c83c78c670bb63e33d395bc67f382acc",
      indexName: "prod_MythicDrops",
    },

    socialLinks: [
      { icon: "discord", link: discord },
      { icon: "github", link: github },
    ],

    footer: {
      message: "Released under the MIT License.",
      copyright:
        "Copyright © 2013-PRESENT Richard Harrah and MythicDrops contributors",
    },

    nav: [
      { text: "Docs", link: "/guide/" },
      {
        text: "Resources",
        items: [
          {
            text: "Team ",
            link: "/team",
          },
          {
            items: [
              {
                text: "Release Notes ",
                link: releases,
              },
              {
                text: "Contributing ",
                link: contributing,
              },
              {
                text: "Javadocs ",
                link: "https://javadoc.io/doc/io.pixeloutlaw.mythicdrops/mythicdrops",
              },
              {
                text: "Discord ",
                link: discord,
              },
            ],
          },
        ],
      },
    ],

    sidebar: {
      "/": [
        {
          text: "Guide",
          collapsible: true,
          items: [
            { text: "Installation", link: "/guide/" },
            {
              text: "Features",
              items: [
                { text: "Tiered Items", link: "/guide/features/tiered-items" },
                { text: "Custom Items", link: "/guide/features/custom-items" },
                {
                  text: "Sockets",
                  items: [
                    {
                      text: "Socket Gems",
                      link: "/guide/features/sockets/gems",
                    },
                    {
                      text: "Socket Extenders",
                      link: "/guide/features/sockets/extenders",
                    },
                    {
                      text: "Socket Gem Combiners",
                      link: "/guide/features/sockets/combiners",
                    },
                  ],
                },
                {
                  text: "Identification",
                  link: "/guide/features/identification",
                },
              ],
            },
            { text: "Commands", link: "/guide/commands" },
            { text: "Permissions", link: "/guide/permissions" },
          ],
        },
        {
          text: "Config Reference",
          collapsible: true,
          items: [
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
            { text: "socketTypes.yml", link: "/config/socketTypes.yml.html" },
            {
              text: "resources Directory",
              link: "/config/resources-directory.html",
            },
            { text: "tiers Directory", link: "/config/tiers-directory.html" },
          ],
        },
      ],
    },
  },
});
