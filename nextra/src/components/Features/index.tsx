import clsx from "clsx";
import { useRouter } from "next/router";

import styles from "./index.module.css";

const Feature = ({ text, icon }) => (
  <div className={styles.feature}>
    {icon}
    <h4>{text}</h4>
  </div>
);

const EmptyFeature = () => (
  <div className={clsx(styles.feature, styles["hide-on-mobile"])} />
);

const TITLE_WITH_TRANSLATIONS = {
  "en-US": "Action RPG drops in Spigot",
};

const FEATURES_WITH_TRANSLATIONS = {
  "en-US": {
    tieredItems: "Tiered Items",
    customItems: "Custom Items",
    sockets: "Sockets",
    identification: "Identification",
    repairing: "Repairing",
    relations: "Relations",
    fullyConfigurable: "Fully Configurable",
  },
};

export const Features = () => {
  const { locale, defaultLocale } = useRouter();

  const featureText = (
    key: keyof typeof FEATURES_WITH_TRANSLATIONS["en-US"]
  ): string =>
    FEATURES_WITH_TRANSLATIONS[locale]?.[key] ??
    FEATURES_WITH_TRANSLATIONS[defaultLocale][key]; // Fallback for missing translations

  return (
    <div className="mx-auto max-w-full w-[880px] text-center px-4 mb-10">
      <p className="text-lg mb-2 text-gray-600 md:!text-2xl">
        {TITLE_WITH_TRANSLATIONS[locale]}
      </p>
      <div className={styles.features}>
        <Feature
          text={featureText("tieredItems")}
          icon={
            <svg
              width="24"
              height="24"
              viewBox="0 0 100 100"
              stroke="currentColor"
              strokeWidth="2"
              fill="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="m73.73 21.156-2.9609-15.926c-0.058593-0.32031-0.26953-0.59375-0.56641-0.72656-0.13281-0.0625-0.27344-0.089844-0.41406-0.089844-0.17578 0-0.35156 0.046876-0.50781 0.13672l-13.961 8.2109c-0.19922 0.11719-0.35156 0.30078-0.43359 0.51562l-18.008 48.941c-2.7344-1.625-5.0859-3.4805-7.5039-5.9062-0.1875-0.19141-0.44531-0.29688-0.71094-0.29688-0.058593 0-0.11719 0.003906-0.17578 0.015625-0.32422 0.058594-0.59766 0.26953-0.73438 0.57031l-1.7305 3.7969c-0.17578 0.38672-0.089844 0.83984 0.21484 1.1328 3.2188 3.1094 5.6094 5.1367 9.2148 7.1289l-8.6406 18.953-0.30469-0.14062c-0.13281-0.0625-0.27344-0.089844-0.41406-0.089844-0.37891 0-0.74219 0.21875-0.91016 0.58594l-1.1094 2.4258c-0.10938 0.24219-0.12109 0.51562-0.027344 0.76562 0.09375 0.24609 0.28125 0.44922 0.52344 0.5625l9.7148 4.4297c0.13281 0.058594 0.27344 0.089844 0.41406 0.089844 0.11719 0 0.23828-0.019532 0.35156-0.0625 0.25-0.09375 0.44922-0.28125 0.55859-0.51953l1.1094-2.4297c0.10938-0.24219 0.12109-0.51562 0.027344-0.76562s-0.28125-0.44922-0.52344-0.55859l-0.30469-0.14062 8.6406-18.949c3.8711 1.4141 6.9688 1.8906 11.426 2.2773 0.027344 0.003906 0.058594 0.003906 0.085937 0.003906 0.39063 0 0.74609-0.22656 0.91016-0.58594l1.7305-3.7969c0.13672-0.30078 0.11719-0.64453-0.050782-0.92578-0.16797-0.28125-0.46484-0.46484-0.79297-0.48437-2.0547-0.13672-5.8477-0.54688-9.3906-1.7656l25.145-45.715c0.11328-0.20703 0.14844-0.44141 0.10938-0.66797z" />
            </svg>
          }
        />
        <Feature
          text={featureText("customItems")}
          icon={
            <svg
              viewBox="0 0 100 100"
              width="24"
              height="24"
              stroke="currentColor"
              strokeWidth="2"
              fill="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="m86.27 50.371-14.93-3.46 12-9.52a9.48 9.48 0 0 0 1.316-13.125 9.479 9.479 0 0 0-13.078-1.735l-12 9.52V16.73a9.449 9.449 0 0 0-2.754-6.711 9.476 9.476 0 0 0-6.695-2.801c-5.223 0-9.46 4.227-9.48 9.453L40.602 32l-12-9.59a9.471 9.471 0 0 0-7.137-2.449 9.479 9.479 0 0 0-4.676 17.23l12 9.59-15 3.36c-5.11 1.148-8.316 6.222-7.168 11.328 1.149 5.11 6.219 8.32 11.328 7.172l14.95-3.36-6.688 13.777A9.485 9.485 0 0 0 30.6 91.73c4.711 2.285 10.383.32 12.668-4.39L50 73.557l6.602 13.828v.004c2.254 4.723 7.915 6.727 12.637 4.47a9.483 9.483 0 0 0 4.473-12.641l-6.652-13.84 14.94 3.46a9.48 9.48 0 0 0 10.238-14.266 9.483 9.483 0 0 0-5.956-4.202z" />
            </svg>
          }
        />
        <Feature
          text={featureText("sockets")}
          icon={
            <svg
              viewBox="0 0 100 100"
              width="24"
              height="24"
              stroke="currentColor"
              strokeWidth="2"
              fill="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="m61.629 82.891-4.785-11.547 6.433-6.434 11.547 4.786zm-6.184-12.629H44.722l-6.75-6.75-.004-27.152 6.75-6.75h10.723l6.75 6.75v27.152zM63.29 34.95l-6.433-6.434L61.64 16.97l13.207 13.207zm-8.066-7.102H44.946L40.16 16.301h19.836zm-11.902.668-6.434 6.434-11.547-4.785 13.207-13.207zm-7.113 8.067V63.29L24.66 68.075V31.798zm.667 28.328 6.434 6.433-4.785 11.547L25.34 69.696zm8.07 7.113h10.278l4.785 11.547-19.852.004zm19.013-8.734V36.583l11.547-4.786v36.277z" />
            </svg>
          }
        />
        <Feature
          text={featureText("identification")}
          icon={
            <svg
              viewBox="0 0 100 100"
              width="24"
              height="24"
              stroke="currentColor"
              strokeWidth="2"
              fill="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="m71.258 42.641-18.391 26.25-21.816-3-3.324-7.453 23.527 3.23 22.152-32.176-27.262-3.746-23.301 32.02.629.086-.532.235 4.731 10.602 1.64 1.23 24.263 3.336 2.02-.887 19.124-27.3z" />
            </svg>
          }
        />
        <EmptyFeature />
        <Feature
          text={featureText("repairing")}
          icon={
            <svg
              viewBox="0 0 100 100"
              width="24"
              height="24"
              stroke="currentColor"
              strokeWidth="2"
              fill="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M85.613 24.969v2.504a4.031 4.031 0 0 1-3.39 1.852c-1.32 0-2.493-.641-3.227-1.622V24H24.78v.97H.093v1.237c.93 11.715 11.688 19.36 21.512 20.016.222.016.445.024.664.032 3.687.351 6.543 3.496 6.543 7.273 0 2.02-.824 3.848-2.156 5.164H24.39v17.31h54.383v-17.31h-2.266a7.425 7.425 0 1 1 9.11-11.168v.196h14.289V24.97z" />
            </svg>
          }
        />
        <Feature
          text={featureText("relations")}
          icon={
            <svg
              viewBox="0 0 100 100"
              width="24"
              height="24"
              stroke="currentColor"
              strokeWidth="2"
              fill="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M76.102 33.102h-29V42.5h29.102c4.102 0 7.5 3.399 7.5 7.5 0 4.2-2.3 7.5-4.2 7.5h-4.1v-4.898L59.2 62 75.403 71.4V67h4.102c7.5 0 13.602-7.601 13.602-16.898-.004-9.3-7.707-17-17.004-17zM53.199 57.398H24.101c-4.102 0-7.5-3.398-7.5-7.5 0-4.2 2.3-7.5 4.2-7.5h3.6v4.7l16.204-9.4L24.402 28.3v4.602H20.8c-7.5 0-13.602 7.602-13.602 16.898 0 9.3 7.602 16.898 16.898 16.898h29.102z" />
            </svg>
          }
        />
        <Feature
          text={featureText("fullyConfigurable")}
          icon={
            <svg
              viewBox="0 0 100 100"
              width="24"
              height="24"
              stroke="currentColor"
              strokeWidth="2"
              fill="currentColor"
              strokeLinecap="round"
              strokeLinejoin="round"
            >
              <path d="M93.391 57.586V42.418l-9.887-2.46a33.85 33.85 0 0 0-2.707-6.556l5.25-8.722-10.723-10.727-8.726 5.25a34.596 34.596 0 0 0-6.551-2.703L57.59 6.613H42.422L39.96 16.5a34.702 34.702 0 0 0-6.55 2.703l-8.727-5.25L13.957 24.68l5.25 8.726a34.679 34.679 0 0 0-2.707 6.547l-9.883 2.461-.008 15.168 9.89 2.461a35.142 35.142 0 0 0 2.704 6.547l-5.25 8.723 10.727 10.73 8.726-5.25a34.938 34.938 0 0 0 6.547 2.707l2.461 9.882 15.172.004 2.457-9.89a34.701 34.701 0 0 0 6.55-2.703l8.727 5.25 10.727-10.727-5.25-8.727a33.995 33.995 0 0 0 2.707-6.555zM50 65.93c-8.793-.004-15.926-7.133-15.926-15.93s7.129-15.93 15.93-15.93c8.793 0 15.93 7.133 15.93 15.93S58.801 65.93 50 65.93z" />
            </svg>
          }
        />
      </div>
    </div>
  );
};
