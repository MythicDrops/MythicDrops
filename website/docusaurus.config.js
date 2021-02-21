const versions = require("./versions.json");

const isDev = process.env.NODE_ENV === "development";
const isVersioningDisabled = !!process.env.DISABLE_VERSIONING;

module.exports = {
  title: "MythicDrops",
  tagline: "Action RPG style drops in Spigot",
  url: "https://pixeloutlaw.github.io",
  baseUrl: "/MythicDrops/",
  baseUrlIssueBanner: true,
  favicon: "img/favicon.ico",
  organizationName: "MythicDrops",
  projectName: "MythicDrops",
  onBrokenLinks: "throw",
  onBrokenMarkdownLinks: "warn",
  presets: [
    [
      "@docusaurus/preset-classic",
      {
        docs: {
          path: "docs",
          editUrl:
            "https://github.com/MythicDrops/MythicDrops/edit/main/website/",
          sidebarPath: require.resolve("./sidebars.js"),
          showLastUpdateTime: true,
          disableVersioning: isVersioningDisabled,
          lastVersion: isDev ? "current" : undefined,
          onlyIncludeVersions:
            !isVersioningDisabled && isDev
              ? ["current", ...versions.slice(0, 2)]
              : undefined,
          versions: {
            current: {
              label: `Current 🚧`,
            },
          },
        },
        theme: {
          customCss: require.resolve("./src/css/custom.css"),
        },
      },
    ],
  ],
  themeConfig: {
    hideableSidebar: true,
    colorMode: {
      defaultMode: "light",
      disableSwitch: false,
      respectPrefersColorScheme: true,
    },
    prism: {
      theme: require("prism-react-renderer/themes/github"),
      darkTheme: require("prism-react-renderer/themes/dracula"),
    },
    navbar: {
      hideOnScroll: true,
      title: "MythicDrops",
      logo: {
        alt: "MythicDrops Logo",
        src: "img/logo.svg",
      },
      items: [
        {
          type: "doc",
          position: "left",
          docId: "installation",
          label: "Docs",
        },
        {
          position: "left",
          to: "support",
          label: "Support",
        },
        {
          href: "https://javadoc.io/doc/io.pixeloutlaw.mythicdrops/mythicdrops",
          label: "Javadocs",
          position: "left",
        },
        {
          type: "docsVersionDropdown",
          position: "right",
          dropdownActiveClassDisabled: true,
          dropdownItemsAfter: [
            {
              to: "/versions",
              label: "All versions",
            },
          ],
        },
        {
          href: "https://github.com/PixelOutlaw/MythicDrops",
          position: "right",
          className: "header-github-link",
          "aria-label": "GitHub repository",
        },
      ],
    },
    footer: {
      style: "dark",
      copyright: `Copyright © ${new Date().getFullYear()} Richard Harrah. Built with Docusaurus.`,
      links: [
        {
          title: "Community",
          items: [
            {
              label: "Discord",
              href: "https://discord.gg/zdMmuUb7wj",
            },
          ],
        },
        {
          title: "More",
          items: [
            {
              label: "GitHub",
              href: "https://github.com/MythicDrops/MythicDrops",
            },
          ],
        },
      ],
    },
  },
};
