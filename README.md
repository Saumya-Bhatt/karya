# Karya

Distributed, scalable Task Scheduler built for high throughput.

- [How to contribute](.github/CONTRIBUTING.md)
- [Architecture Overview](./docs/documentation/OVERVIEW)
- [API Docs](https://saumya-bhatt.github.io/karya/)

Overview:

- [Components](./docs/documentation/COMPONENTS.md)
- [Data Adapters](./docs/documentation/DATA_ADAPTERS.md)
- [Connectors](./docs/documentation/CONNECTORS.md)
- [Hooks](./docs/documentation/HOOKS.md)

---

## Use cases

1. Schedule tasks to run at periodic frequency.
2. Schedule async, non-recurring tasks at scale.
3. Chain recurring and non-recurring tasks together to achieve custom flow.

---

## Why Karya?

There are several task schedulers out there. Why to choose Karya? Here are the reasons:

1. >**Built for high throughput**.
   
    It follows the philosophy: *solve scaling by throwing money at it*, which means one just needs to add one more node and scale horizontally infinitely!

2. >**Fault-tolerant by nature**

   Software like Postgres and Redis have already solved the problem of achieving fault tolerance. Karya nodes are stateless in nature and utilizes the properties of such [data-adapters](./docs/documentation/DATA_ADAPTERS.md) to achieve this goal.

3. >**Highly Pluggable and customizable**

    Be it in terms of [data-adapters](./docs/documentation/DATA_ADAPTERS.md) or [connectors](#connectors). Just specify the properties in a .yml file, and you're good to go! Karya [components](./docs/documentation/COMPONENTS.md) can be customized to suite your use case endlessly!

4. >**Fast, Performant and Safe**
   
    Being written in Kotlin thereby making it typesafe, it uses coroutines to achieve *structured concurrency* while being thread safe.

---

## Features

- Schedule periodic/delayed tasks with lower bound being *1s*.
- Can be made to work with almost all universally available software via [data-adapters](./docs/documentation/DATA_ADAPTERS.md)
- Ability to [chain plans](./docs/documentation/CONNECTORS.md/#chained-plans) (be it periodic or delayed). 
- Attach [hooks](./docs/documentation/HOOKS.md) to task states (`ON_FAILURE`,`ON_COMPLETION`)
  - Setup notifications/alerts/chain another task using the same interface! (**Eat What You Kill**)
  - Multiple hooks can be attached to the task for same states.
- Get comprehensive summary of the task running/previous tasks including error logs if any!

---
