/**
 * Copyright (c) 2017-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

module.exports = {
  title: "MythicDrops",
  tagline: "Action RPG style drops in Spigot",
  url: "https://pixeloutlaw.github.io",
  baseUrl: "/MythicDrops/",
  favicon: "img/favicon.ico",
  organizationName: "PixelOutlaw", // Usually your GitHub org/user name.
  projectName: "MythicDrops", // Usually your repo name.
  themeConfig: {
    navbar: {
      title: "MythicDrops",
      logo: {
        alt: "MythicDrops Logo",
        src: "img/logo.svg",
      },
      items: [
        { to: "docs/installation", label: "Docs", position: "left" },
        { to: "support", label: "Support", position: "left" },
        {
          href: "https://github.com/PixelOutlaw/MythicDrops",
          label: "GitHub",
          position: "right",
        },
      ],
    },
    footer: {
      style: "dark",
      copyright: `Copyright © ${new Date().getFullYear()} Richard Harrah. Built with Docusaurus.`,
      links: [],
    },
  },
  presets: [
    [
      "@docusaurus/preset-classic",
      {
        docs: {
          path: "../docs",
          sidebarPath: require.resolve("./sidebars.js"),
        },
        theme: {
          customCss: require.resolve("./src/css/custom.css"),
        },
      },
    ],
  ],
};
