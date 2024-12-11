# Contributing to Karya

Firstly, thank you for considering contributing to Karya! Here are a few guidelines to help you get started.

## Getting Started

1. Go through the [Architecture Overview](../docs/documentation/OVERVIEW.md) to understand the components of Karya.
2. Set up the application locally by following the [Local Setup](../docs/documentation/LOCAL_SETUP.md) guide.
3. Follow the contribution guidelines mentioned below.
4. Fork the repository and start contributing!

## Linting and Formatting

- [Detekt](https://detekt.dev/) Plugin is being used to enforce code style and formatting
- This is part of the build step hence ensure `./gradlew detekt` runs successfully for the build to succeed.
- Ruleset can be found [here](../configs/detekt.yml)

<details>
<summary><strong>Additional configs for IntelliJ users</strong></summary>

### Set the indentation to space : 2

![indentation_settings](../docs/media/intellij_indentation.png)

### While running the Intellij Formatter, check the below options

![format_settings](../docs/media/intellij_format.png)

</details>
