/*!
 *
 * litepicker.umd.js
 * Litepicker v2.0.12 (https://github.com/wakirin/Litepicker)
 * Package: litepicker (https://www.npmjs.com/package/litepicker)
 * License: MIT (https://github.com/wakirin/Litepicker/blob/master/LICENCE.md)
 * Copyright 2019-2021 Rinat G.
 *
 * Hash: f46190e781537007f2f2
 *
 */
!(function (t, e) {
  'object' == typeof exports && 'object' == typeof module
    ? (module.exports = e())
    : 'function' == typeof define && define.amd
    ? define('Litepicker', [], e)
    : 'object' == typeof exports
    ? (exports.Litepicker = e())
    : (t.Litepicker = e());
})(window, function () {
  return (function (t) {
    var e = {};
    function n(i) {
      if (e[i]) return e[i].exports;
      var o = (e[i] = { i: i, l: !1, exports: {} });
      return t[i].call(o.exports, o, o.exports, n), (o.l = !0), o.exports;
    }
    return (
      (n.m = t),
      (n.c = e),
      (n.d = function (t, e, i) {
        n.o(t, e) || Object.defineProperty(t, e, { enumerable: !0, get: i });
      }),
      (n.r = function (t) {
        'undefined' != typeof Symbol && Symbol.toStringTag && Object.defineProperty(t, Symbol.toStringTag, { value: 'Module' }),
          Object.defineProperty(t, '__esModule', { value: !0 });
      }),
      (n.t = function (t, e) {
        if ((1 & e && (t = n(t)), 8 & e)) return t;
        if (4 & e && 'object' == typeof t && t && t.__esModule) return t;
        var i = Object.create(null);
        if ((n.r(i), Object.defineProperty(i, 'default', { enumerable: !0, value: t }), 2 & e && 'string' != typeof t))
          for (var o in t)
            n.d(
              i,
              o,
              function (e) {
                return t[e];
              }.bind(null, o)
            );
        return i;
      }),
      (n.n = function (t) {
        var e =
          t && t.__esModule
            ? function () {
                return t.default;
              }
            : function () {
                return t;
              };
        return n.d(e, 'a', e), e;
      }),
      (n.o = function (t, e) {
        return Object.prototype.hasOwnProperty.call(t, e);
      }),
      (n.p = ''),
      n((n.s = 4))
    );
  })([
    function (t, e, n) {
      'use strict';
      Object.defineProperty(e, '__esModule', { value: !0 });
      var i = (function () {
        function t(e, n, i) {
          void 0 === e && (e = null),
            void 0 === n && (n = null),
            void 0 === i && (i = 'en-US'),
            (this.dateInstance =
              'object' == typeof n && null !== n
                ? n.parse(e instanceof t ? e.clone().toJSDate() : e)
                : 'string' == typeof n
                ? t.parseDateTime(e, n, i)
                : e
                ? t.parseDateTime(e)
                : t.parseDateTime(new Date())),
            (this.lang = i);
        }
        return (
          (t.parseDateTime = function (e, n, i) {
            if ((void 0 === n && (n = 'YYYY-MM-DD'), void 0 === i && (i = 'en-US'), !e)) return new Date(NaN);
            if (e instanceof Date) return new Date(e);
            if (e instanceof t) return e.clone().toJSDate();
            if (/^-?\d{10,}$/.test(e)) return t.getDateZeroTime(new Date(Number(e)));
            if ('string' == typeof e) {
              for (var o = [], s = null; null != (s = t.regex.exec(n)); ) '\\' !== s[1] && o.push(s);
              if (o.length) {
                var r = { year: null, month: null, shortMonth: null, longMonth: null, day: null, value: '' };
                o[0].index > 0 && (r.value += '.*?');
                for (var a = 0, h = Object.entries(o); a < h.length; a++) {
                  var l = h[a],
                    p = l[0],
                    c = l[1],
                    d = Number(p),
                    u = t.formatPatterns(c[0], i),
                    m = u.group,
                    f = u.pattern;
                  (r[m] = d + 1), (r.value += f), (r.value += '.*?');
                }
                var g = new RegExp('^' + r.value + '$');
                if (g.test(e)) {
                  var y = g.exec(e),
                    v = Number(y[r.year]),
                    D = null;
                  r.month
                    ? (D = Number(y[r.month]) - 1)
                    : r.shortMonth
                    ? (D = t.shortMonths(i).indexOf(y[r.shortMonth]))
                    : r.longMonth && (D = t.longMonths(i).indexOf(y[r.longMonth]));
                  var w = Number(y[r.day]) || 1;
                  return new Date(v, D, w, 0, 0, 0, 0);
                }
              }
            }
            return t.getDateZeroTime(new Date(e));
          }),
          (t.convertArray = function (e, n) {
            return e.map(function (e) {
              return e instanceof Array
                ? e.map(function (e) {
                    return new t(e, n);
                  })
                : new t(e, n);
            });
          }),
          (t.getDateZeroTime = function (t) {
            return new Date(t.getFullYear(), t.getMonth(), t.getDate(), 0, 0, 0, 0);
          }),
          (t.shortMonths = function (e) {
            return t.MONTH_JS.map(function (t) {
              return new Date(2019, t).toLocaleString(e, { month: 'short' });
            });
          }),
          (t.longMonths = function (e) {
            return t.MONTH_JS.map(function (t) {
              return new Date(2019, t).toLocaleString(e, { month: 'long' });
            });
          }),
          (t.formatPatterns = function (e, n) {
            switch (e) {
              case 'YY':
              case 'YYYY':
                return { group: 'year', pattern: '(\\d{' + e.length + '})' };
              case 'M':
                return { group: 'month', pattern: '(\\d{1,2})' };
              case 'MM':
                return { group: 'month', pattern: '(\\d{2})' };
              case 'MMM':
                return { group: 'shortMonth', pattern: '(' + t.shortMonths(n).join('|') + ')' };
              case 'MMMM':
                return { group: 'longMonth', pattern: '(' + t.longMonths(n).join('|') + ')' };
              case 'D':
                return { group: 'day', pattern: '(\\d{1,2})' };
              case 'DD':
                return { group: 'day', pattern: '(\\d{2})' };
            }
          }),
          (t.prototype.toJSDate = function () {
            return this.dateInstance;
          }),
          (t.prototype.toLocaleString = function (t, e) {
            return this.dateInstance.toLocaleString(t, e);
          }),
          (t.prototype.toDateString = function () {
            return this.dateInstance.toDateString();
          }),
          (t.prototype.getSeconds = function () {
            return this.dateInstance.getSeconds();
          }),
          (t.prototype.getDay = function () {
            return this.dateInstance.getDay();
          }),
          (t.prototype.getTime = function () {
            return this.dateInstance.getTime();
          }),
          (t.prototype.getDate = function () {
            return this.dateInstance.getDate();
          }),
          (t.prototype.getMonth = function () {
            return this.dateInstance.getMonth();
          }),
          (t.prototype.getFullYear = function () {
            return this.dateInstance.getFullYear();
          }),
          (t.prototype.setMonth = function (t) {
            return this.dateInstance.setMonth(t);
          }),
          (t.prototype.setHours = function (t, e, n, i) {
            void 0 === t && (t = 0),
              void 0 === e && (e = 0),
              void 0 === n && (n = 0),
              void 0 === i && (i = 0),
              this.dateInstance.setHours(t, e, n, i);
          }),
          (t.prototype.setSeconds = function (t) {
            return this.dateInstance.setSeconds(t);
          }),
          (t.prototype.setDate = function (t) {
            return this.dateInstance.setDate(t);
          }),
          (t.prototype.setFullYear = function (t) {
            return this.dateInstance.setFullYear(t);
          }),
          (t.prototype.getWeek = function (t) {
            var e = new Date(this.timestamp()),
              n = (this.getDay() + (7 - t)) % 7;
            e.setDate(e.getDate() - n);
            var i = e.getTime();
            return (
              e.setMonth(0, 1), e.getDay() !== t && e.setMonth(0, 1 + ((4 - e.getDay() + 7) % 7)), 1 + Math.ceil((i - e.getTime()) / 6048e5)
            );
          }),
          (t.prototype.clone = function () {
            return new t(this.toJSDate());
          }),
          (t.prototype.isBetween = function (t, e, n) {
            switch ((void 0 === n && (n = '()'), n)) {
              default:
              case '()':
                return this.timestamp() > t.getTime() && this.timestamp() < e.getTime();
              case '[)':
                return this.timestamp() >= t.getTime() && this.timestamp() < e.getTime();
              case '(]':
                return this.timestamp() > t.getTime() && this.timestamp() <= e.getTime();
              case '[]':
                return this.timestamp() >= t.getTime() && this.timestamp() <= e.getTime();
            }
          }),
          (t.prototype.isBefore = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return t.getTime() > this.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime() >
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(t.getFullYear(), t.getMonth(), 1).getTime() > new Date(this.getFullYear(), this.getMonth(), 1).getTime();
              case 'year':
              case 'years':
                return t.getFullYear() > this.getFullYear();
            }
            throw new Error('isBefore: Invalid unit!');
          }),
          (t.prototype.isSameOrBefore = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return t.getTime() >= this.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime() >=
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(t.getFullYear(), t.getMonth(), 1).getTime() >= new Date(this.getFullYear(), this.getMonth(), 1).getTime();
            }
            throw new Error('isSameOrBefore: Invalid unit!');
          }),
          (t.prototype.isAfter = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return this.getTime() > t.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime() >
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(this.getFullYear(), this.getMonth(), 1).getTime() > new Date(t.getFullYear(), t.getMonth(), 1).getTime();
              case 'year':
              case 'years':
                return this.getFullYear() > t.getFullYear();
            }
            throw new Error('isAfter: Invalid unit!');
          }),
          (t.prototype.isSameOrAfter = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return this.getTime() >= t.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime() >=
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(this.getFullYear(), this.getMonth(), 1).getTime() >= new Date(t.getFullYear(), t.getMonth(), 1).getTime();
            }
            throw new Error('isSameOrAfter: Invalid unit!');
          }),
          (t.prototype.isSame = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                return this.getTime() === t.getTime();
              case 'day':
              case 'days':
                return (
                  new Date(this.getFullYear(), this.getMonth(), this.getDate()).getTime() ===
                  new Date(t.getFullYear(), t.getMonth(), t.getDate()).getTime()
                );
              case 'month':
              case 'months':
                return new Date(this.getFullYear(), this.getMonth(), 1).getTime() === new Date(t.getFullYear(), t.getMonth(), 1).getTime();
            }
            throw new Error('isSame: Invalid unit!');
          }),
          (t.prototype.add = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                this.setSeconds(this.getSeconds() + t);
                break;
              case 'day':
              case 'days':
                this.setDate(this.getDate() + t);
                break;
              case 'month':
              case 'months':
                this.setMonth(this.getMonth() + t);
            }
            return this;
          }),
          (t.prototype.subtract = function (t, e) {
            switch ((void 0 === e && (e = 'seconds'), e)) {
              case 'second':
              case 'seconds':
                this.setSeconds(this.getSeconds() - t);
                break;
              case 'day':
              case 'days':
                this.setDate(this.getDate() - t);
                break;
              case 'month':
              case 'months':
                this.setMonth(this.getMonth() - t);
            }
            return this;
          }),
          (t.prototype.diff = function (t, e) {
            void 0 === e && (e = 'seconds');
            switch (e) {
              default:
              case 'second':
              case 'seconds':
                return this.getTime() - t.getTime();
              case 'day':
              case 'days':
                return Math.round((this.timestamp() - t.getTime()) / 864e5);
              case 'month':
              case 'months':
            }
          }),
          (t.prototype.format = function (e, n) {
            if ((void 0 === n && (n = 'en-US'), 'object' == typeof e)) return e.output(this.clone().toJSDate());
            for (var i = '', o = [], s = null; null != (s = t.regex.exec(e)); ) '\\' !== s[1] && o.push(s);
            if (o.length) {
              o[0].index > 0 && (i += e.substring(0, o[0].index));
              for (var r = 0, a = Object.entries(o); r < a.length; r++) {
                var h = a[r],
                  l = h[0],
                  p = h[1],
                  c = Number(l);
                (i += this.formatTokens(p[0], n)),
                  o[c + 1] && (i += e.substring(p.index + p[0].length, o[c + 1].index)),
                  c === o.length - 1 && (i += e.substring(p.index + p[0].length));
              }
            }
            return i.replace(/\\/g, '');
          }),
          (t.prototype.timestamp = function () {
            return new Date(this.getFullYear(), this.getMonth(), this.getDate(), 0, 0, 0, 0).getTime();
          }),
          (t.prototype.formatTokens = function (e, n) {
            switch (e) {
              case 'YY':
                return String(this.getFullYear()).slice(-2);
              case 'YYYY':
                return String(this.getFullYear());
              case 'M':
                return String(this.getMonth() + 1);
              case 'MM':
                return ('0' + (this.getMonth() + 1)).slice(-2);
              case 'MMM':
                return t.shortMonths(n)[this.getMonth()];
              case 'MMMM':
                return t.longMonths(n)[this.getMonth()];
              case 'D':
                return String(this.getDate());
              case 'DD':
                return ('0' + this.getDate()).slice(-2);
              default:
                return '';
            }
          }),
          (t.regex = /(\\)?(Y{2,4}|M{1,4}|D{1,2}|d{1,4})/g),
          (t.MONTH_JS = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11]),
          t
        );
      })();
      e.DateTime = i;
    },
    function (t, e, n) {
      'use strict';
      var i,
        o =
          (this && this.__extends) ||
          ((i = function (t, e) {
            return (i =
              Object.setPrototypeOf ||
              ({ __proto__: [] } instanceof Array &&
                function (t, e) {
                  t.__proto__ = e;
                }) ||
              function (t, e) {
                for (var n in e) e.hasOwnProperty(n) && (t[n] = e[n]);
              })(t, e);
          }),
          function (t, e) {
            function n() {
              this.constructor = t;
            }
            i(t, e), (t.prototype = null === e ? Object.create(e) : ((n.prototype = e.prototype), new n()));
          }),
        s =
          (this && this.__spreadArrays) ||
          function () {
            for (var t = 0, e = 0, n = arguments.length; e < n; e++) t += arguments[e].length;
            var i = Array(t),
              o = 0;
            for (e = 0; e < n; e++) for (var s = arguments[e], r = 0, a = s.length; r < a; r++, o++) i[o] = s[r];
            return i;
          };
      Object.defineProperty(e, '__esModule', { value: !0 });
      var r = n(5),
        a = n(0),
        h = n(3),
        l = n(2),
        p = (function (t) {
          function e(e) {
            var n = t.call(this, e) || this;
            return (n.preventClick = !1), n.bindEvents(), n;
          }
          return (
            o(e, t),
            (e.prototype.scrollToDate = function (t) {
              if (this.options.scrollToDate) {
                var e = this.options.startDate instanceof a.DateTime ? this.options.startDate.clone() : null,
                  n = this.options.endDate instanceof a.DateTime ? this.options.endDate.clone() : null;
                !this.options.startDate || (t && t !== this.options.element)
                  ? t &&
                    this.options.endDate &&
                    t === this.options.elementEnd &&
                    (n.setDate(1),
                    this.options.numberOfMonths > 1 && n.isAfter(e) && n.setMonth(n.getMonth() - (this.options.numberOfMonths - 1)),
                    (this.calendars[0] = n.clone()))
                  : (e.setDate(1), (this.calendars[0] = e.clone()));
              }
            }),
            (e.prototype.bindEvents = function () {
              document.addEventListener('click', this.onClick.bind(this), !0),
                (this.ui = document.createElement('div')),
                (this.ui.className = h.litepicker),
                (this.ui.style.display = 'none'),
                this.ui.addEventListener('mouseenter', this.onMouseEnter.bind(this), !0),
                this.ui.addEventListener('mouseleave', this.onMouseLeave.bind(this), !1),
                this.options.autoRefresh
                  ? (this.options.element instanceof HTMLElement &&
                      this.options.element.addEventListener('keyup', this.onInput.bind(this), !0),
                    this.options.elementEnd instanceof HTMLElement &&
                      this.options.elementEnd.addEventListener('keyup', this.onInput.bind(this), !0))
                  : (this.options.element instanceof HTMLElement &&
                      this.options.element.addEventListener('change', this.onInput.bind(this), !0),
                    this.options.elementEnd instanceof HTMLElement &&
                      this.options.elementEnd.addEventListener('change', this.onInput.bind(this), !0)),
                this.options.parentEl
                  ? this.options.parentEl instanceof HTMLElement
                    ? this.options.parentEl.appendChild(this.ui)
                    : document.querySelector(this.options.parentEl).appendChild(this.ui)
                  : this.options.inlineMode
                  ? this.options.element instanceof HTMLInputElement
                    ? this.options.element.parentNode.appendChild(this.ui)
                    : this.options.element.appendChild(this.ui)
                  : document.body.appendChild(this.ui),
                this.updateInput(),
                this.init(),
                'function' == typeof this.options.setup && this.options.setup.call(this, this),
                this.render(),
                this.options.inlineMode && this.show();
            }),
            (e.prototype.updateInput = function () {
              if (this.options.element instanceof HTMLInputElement) {
                var t = this.options.startDate,
                  e = this.options.endDate;
                if (this.options.singleMode && t) this.options.element.value = t.format(this.options.format, this.options.lang);
                else if (!this.options.singleMode && t && e) {
                  var n = t.format(this.options.format, this.options.lang),
                    i = e.format(this.options.format, this.options.lang);
                  this.options.elementEnd instanceof HTMLInputElement
                    ? ((this.options.element.value = n), (this.options.elementEnd.value = i))
                    : (this.options.element.value = '' + n + this.options.delimiter + i);
                }
                t ||
                  e ||
                  ((this.options.element.value = ''),
                  this.options.elementEnd instanceof HTMLInputElement && (this.options.elementEnd.value = ''));
              }
            }),
            (e.prototype.isSamePicker = function (t) {
              return t.closest('.' + h.litepicker) === this.ui;
            }),
            (e.prototype.shouldShown = function (t) {
              return !t.disabled && (t === this.options.element || (this.options.elementEnd && t === this.options.elementEnd));
            }),
            (e.prototype.shouldResetDatePicked = function () {
              return this.options.singleMode || 2 === this.datePicked.length;
            }),
            (e.prototype.shouldSwapDatePicked = function () {
              return 2 === this.datePicked.length && this.datePicked[0].getTime() > this.datePicked[1].getTime();
            }),
            (e.prototype.shouldCheckLockDays = function () {
              return this.options.disallowLockDaysInRange && 2 === this.datePicked.length;
            }),
            (e.prototype.onClick = function (t) {
              var e = t.target;
              if ((t.target.shadowRoot && (e = t.composedPath()[0]), e && this.ui))
                if (this.shouldShown(e)) this.show(e);
                else if (e.closest('.' + h.litepicker) || !this.isShowning()) {
                  if (this.isSamePicker(e))
                    if ((this.emit('before:click', e), this.preventClick)) this.preventClick = !1;
                    else {
                      if (e.classList.contains(h.dayItem)) {
                        if ((t.preventDefault(), e.classList.contains(h.isLocked))) return;
                        if (
                          (this.shouldResetDatePicked() && (this.datePicked.length = 0),
                          (this.datePicked[this.datePicked.length] = new a.DateTime(e.dataset.time)),
                          this.shouldSwapDatePicked())
                        ) {
                          var n = this.datePicked[1].clone();
                          (this.datePicked[1] = this.datePicked[0].clone()), (this.datePicked[0] = n.clone());
                        }
                        if (this.shouldCheckLockDays())
                          l.rangeIsLocked(this.datePicked, this.options) &&
                            (this.emit('error:range', this.datePicked), (this.datePicked.length = 0));
                        return (
                          this.render(),
                          this.emit.apply(
                            this,
                            s(
                              ['preselect'],
                              s(this.datePicked).map(function (t) {
                                return t.clone();
                              })
                            )
                          ),
                          void (
                            this.options.autoApply &&
                            (this.options.singleMode && this.datePicked.length
                              ? (this.setDate(this.datePicked[0]), this.hide())
                              : this.options.singleMode ||
                                2 !== this.datePicked.length ||
                                (this.setDateRange(this.datePicked[0], this.datePicked[1]), this.hide()))
                          )
                        );
                      }
                      if (e.classList.contains(h.buttonPreviousMonth)) {
                        t.preventDefault();
                        var i = 0,
                          o = this.options.switchingMonths || this.options.numberOfMonths;
                        if (this.options.splitView) {
                          var r = e.closest('.' + h.monthItem);
                          (i = l.findNestedMonthItem(r)), (o = 1);
                        }
                        return (
                          this.calendars[i].setMonth(this.calendars[i].getMonth() - o),
                          this.gotoDate(this.calendars[i], i),
                          void this.emit('change:month', this.calendars[i], i)
                        );
                      }
                      if (e.classList.contains(h.buttonNextMonth)) {
                        t.preventDefault();
                        (i = 0), (o = this.options.switchingMonths || this.options.numberOfMonths);
                        if (this.options.splitView) {
                          r = e.closest('.' + h.monthItem);
                          (i = l.findNestedMonthItem(r)), (o = 1);
                        }
                        return (
                          this.calendars[i].setMonth(this.calendars[i].getMonth() + o),
                          this.gotoDate(this.calendars[i], i),
                          void this.emit('change:month', this.calendars[i], i)
                        );
                      }
                      e.classList.contains(h.buttonCancel) && (t.preventDefault(), this.hide(), this.emit('button:cancel')),
                        e.classList.contains(h.buttonApply) &&
                          (t.preventDefault(),
                          this.options.singleMode && this.datePicked.length
                            ? this.setDate(this.datePicked[0])
                            : this.options.singleMode ||
                              2 !== this.datePicked.length ||
                              this.setDateRange(this.datePicked[0], this.datePicked[1]),
                          this.hide(),
                          this.emit('button:apply', this.options.startDate, this.options.endDate));
                    }
                } else this.hide();
            }),
            (e.prototype.showTooltip = function (t, e) {
              var n = this.ui.querySelector('.' + h.containerTooltip);
              (n.style.visibility = 'visible'), (n.innerHTML = e);
              var i = this.ui.getBoundingClientRect(),
                o = n.getBoundingClientRect(),
                s = t.getBoundingClientRect(),
                r = s.top,
                a = s.left;
              if (this.options.inlineMode && this.options.parentEl) {
                var l = this.ui.parentNode.getBoundingClientRect();
                (r -= l.top), (a -= l.left);
              } else (r -= i.top), (a -= i.left);
              (r -= o.height),
                (a -= o.width / 2),
                (a += s.width / 2),
                (n.style.top = r + 'px'),
                (n.style.left = a + 'px'),
                this.emit('tooltip', n, t);
            }),
            (e.prototype.hideTooltip = function () {
              this.ui.querySelector('.' + h.containerTooltip).style.visibility = 'hidden';
            }),
            (e.prototype.shouldAllowMouseEnter = function (t) {
              return !this.options.singleMode && !t.classList.contains(h.isLocked);
            }),
            (e.prototype.shouldAllowRepick = function () {
              return this.options.elementEnd && this.options.allowRepick && this.options.startDate && this.options.endDate;
            }),
            (e.prototype.isDayItem = function (t) {
              return t.classList.contains(h.dayItem);
            }),
            (e.prototype.onMouseEnter = function (t) {
              var e = this,
                n = t.target;
              if (this.isDayItem(n) && this.shouldAllowMouseEnter(n)) {
                if (
                  (this.shouldAllowRepick() &&
                    (this.triggerElement === this.options.element
                      ? (this.datePicked[0] = this.options.endDate.clone())
                      : this.triggerElement === this.options.elementEnd && (this.datePicked[0] = this.options.startDate.clone())),
                  1 !== this.datePicked.length)
                )
                  return;
                var i = this.ui.querySelector('.' + h.dayItem + '[data-time="' + this.datePicked[0].getTime() + '"]'),
                  o = this.datePicked[0].clone(),
                  s = new a.DateTime(n.dataset.time),
                  r = !1;
                if (o.getTime() > s.getTime()) {
                  var l = o.clone();
                  (o = s.clone()), (s = l.clone()), (r = !0);
                }
                if (
                  (Array.prototype.slice.call(this.ui.querySelectorAll('.' + h.dayItem)).forEach(function (t) {
                    var n = new a.DateTime(t.dataset.time),
                      i = e.renderDay(n);
                    n.isBetween(o, s) && i.classList.add(h.isInRange), (t.className = i.className);
                  }),
                  n.classList.add(h.isEndDate),
                  r
                    ? (i && i.classList.add(h.isFlipped), n.classList.add(h.isFlipped))
                    : (i && i.classList.remove(h.isFlipped), n.classList.remove(h.isFlipped)),
                  this.options.showTooltip)
                ) {
                  var p = s.diff(o, 'day') + 1;
                  if (('function' == typeof this.options.tooltipNumber && (p = this.options.tooltipNumber.call(this, p)), p > 0)) {
                    var c = this.pluralSelector(p),
                      d = p + ' ' + (this.options.tooltipText[c] ? this.options.tooltipText[c] : '[' + c + ']');
                    this.showTooltip(n, d);
                    var u = window.navigator.userAgent,
                      m = /(iphone|ipad)/i.test(u),
                      f = /OS 1([0-2])/i.test(u);
                    m && f && n.dispatchEvent(new Event('click'));
                  } else this.hideTooltip();
                }
              }
            }),
            (e.prototype.onMouseLeave = function (t) {
              t.target;
              this.options.allowRepick &&
                (!this.options.allowRepick || this.options.startDate || this.options.endDate) &&
                ((this.datePicked.length = 0), this.render());
            }),
            (e.prototype.onInput = function (t) {
              var e = this.parseInput(),
                n = e[0],
                i = e[1],
                o = this.options.format;
              if (
                this.options.elementEnd
                  ? n instanceof a.DateTime &&
                    i instanceof a.DateTime &&
                    n.format(o) === this.options.element.value &&
                    i.format(o) === this.options.elementEnd.value
                  : this.options.singleMode
                  ? n instanceof a.DateTime && n.format(o) === this.options.element.value
                  : n instanceof a.DateTime &&
                    i instanceof a.DateTime &&
                    '' + n.format(o) + this.options.delimiter + i.format(o) === this.options.element.value
              ) {
                if (i && n.getTime() > i.getTime()) {
                  var s = n.clone();
                  (n = i.clone()), (i = s.clone());
                }
                (this.options.startDate = new a.DateTime(n, this.options.format, this.options.lang)),
                  i && (this.options.endDate = new a.DateTime(i, this.options.format, this.options.lang)),
                  this.updateInput(),
                  this.render();
                var r = n.clone(),
                  h = 0;
                (this.options.elementEnd ? n.format(o) === t.target.value : t.target.value.startsWith(n.format(o))) ||
                  ((r = i.clone()), (h = this.options.numberOfMonths - 1)),
                  this.emit('selected', this.getStartDate(), this.getEndDate()),
                  this.gotoDate(r, h);
              }
            }),
            e
          );
        })(r.Calendar);
      e.Litepicker = p;
    },
    function (t, e, n) {
      'use strict';
      Object.defineProperty(e, '__esModule', { value: !0 }),
        (e.findNestedMonthItem = function (t) {
          for (var e = t.parentNode.childNodes, n = 0; n < e.length; n += 1) {
            if (e.item(n) === t) return n;
          }
          return 0;
        }),
        (e.dateIsLocked = function (t, e, n) {
          var i = !1;
          return (
            e.lockDays.length &&
              (i = e.lockDays.filter(function (n) {
                return n instanceof Array ? t.isBetween(n[0], n[1], e.lockDaysInclusivity) : n.isSame(t, 'day');
              }).length),
            i || 'function' != typeof e.lockDaysFilter || (i = e.lockDaysFilter.call(this, t.clone(), null, n)),
            i
          );
        }),
        (e.rangeIsLocked = function (t, e) {
          var n = !1;
          return (
            e.lockDays.length &&
              (n = e.lockDays.filter(function (n) {
                if (n instanceof Array) {
                  var i = t[0].toDateString() === n[0].toDateString() && t[1].toDateString() === n[1].toDateString();
                  return n[0].isBetween(t[0], t[1], e.lockDaysInclusivity) || n[1].isBetween(t[0], t[1], e.lockDaysInclusivity) || i;
                }
                return n.isBetween(t[0], t[1], e.lockDaysInclusivity);
              }).length),
            n || 'function' != typeof e.lockDaysFilter || (n = e.lockDaysFilter.call(this, t[0].clone(), t[1].clone(), t)),
            n
          );
        });
    },
    function (t, e, n) {
      t.exports = {
        showWeekNumbers: 'show-week-numbers',
        litepicker: 'litepicker',
        containerMain: 'container__main',
        containerMonths: 'container__months',
        columns2: 'columns-2',
        columns3: 'columns-3',
        columns4: 'columns-4',
        splitView: 'split-view',
        monthItemHeader: 'month-item-header',
        buttonPreviousMonth: 'button-previous-month',
        buttonNextMonth: 'button-next-month',
        monthItem: 'month-item',
        monthItemName: 'month-item-name',
        monthItemYear: 'month-item-year',
        resetButton: 'reset-button',
        monthItemWeekdaysRow: 'month-item-weekdays-row',
        noPreviousMonth: 'no-previous-month',
        noNextMonth: 'no-next-month',
        containerDays: 'container__days',
        dayItem: 'day-item',
        isToday: 'is-today',
        isLocked: 'is-locked',
        isInRange: 'is-in-range',
        isStartDate: 'is-start-date',
        isFlipped: 'is-flipped',
        isEndDate: 'is-end-date',
        isHighlighted: 'is-highlighted',
        weekNumber: 'week-number',
        containerFooter: 'container__footer',
        previewDateRange: 'preview-date-range',
        buttonCancel: 'button-cancel',
        buttonApply: 'button-apply',
        containerTooltip: 'container__tooltip',
      };
    },
    function (t, e, n) {
      'use strict';
      Object.defineProperty(e, '__esModule', { value: !0 });
      var i = n(1);
      (e.Litepicker = i.Litepicker), n(8), (window.Litepicker = i.Litepicker), (e.default = i.Litepicker);
    },
    function (t, e, n) {
      'use strict';
      var i,
        o =
          (this && this.__extends) ||
          ((i = function (t, e) {
            return (i =
              Object.setPrototypeOf ||
              ({ __proto__: [] } instanceof Array &&
                function (t, e) {
                  t.__proto__ = e;
                }) ||
              function (t, e) {
                for (var n in e) e.hasOwnProperty(n) && (t[n] = e[n]);
              })(t, e);
          }),
          function (t, e) {
            function n() {
              this.constructor = t;
            }
            i(t, e), (t.prototype = null === e ? Object.create(e) : ((n.prototype = e.prototype), new n()));
          });
      Object.defineProperty(e, '__esModule', { value: !0 });
      var s = n(6),
        r = n(0),
        a = n(3),
        h = n(2),
        l = (function (t) {
          function e(e) {
            return t.call(this, e) || this;
          }
          return (
            o(e, t),
            (e.prototype.render = function () {
              var t = this;
              this.emit('before:render', this.ui);
              var e = document.createElement('div');
              e.className = a.containerMain;
              var n = document.createElement('div');
              (n.className = a.containerMonths),
                a['columns' + this.options.numberOfColumns] &&
                  (n.classList.remove(a.columns2, a.columns3, a.columns4), n.classList.add(a['columns' + this.options.numberOfColumns])),
                this.options.splitView && n.classList.add(a.splitView),
                this.options.showWeekNumbers && n.classList.add(a.showWeekNumbers);
              for (
                var i = this.calendars[0].clone(), o = i.getMonth(), s = i.getMonth() + this.options.numberOfMonths, r = 0, h = o;
                h < s;
                h += 1
              ) {
                var l = i.clone();
                l.setDate(1),
                  l.setHours(0, 0, 0, 0),
                  this.options.splitView ? (l = this.calendars[r].clone()) : l.setMonth(h),
                  n.appendChild(this.renderMonth(l, r)),
                  (r += 1);
              }
              if (((this.ui.innerHTML = ''), e.appendChild(n), this.options.resetButton)) {
                var p = void 0;
                'function' == typeof this.options.resetButton
                  ? (p = this.options.resetButton.call(this))
                  : (((p = document.createElement('button')).type = 'button'),
                    (p.className = a.resetButton),
                    (p.innerHTML = this.options.buttonText.reset)),
                  p.addEventListener('click', function (e) {
                    e.preventDefault(), t.clearSelection();
                  }),
                  e
                    .querySelector('.' + a.monthItem + ':last-child')
                    .querySelector('.' + a.monthItemHeader)
                    .appendChild(p);
              }
              this.ui.appendChild(e),
                (this.options.autoApply && !this.options.footerHTML) || this.ui.appendChild(this.renderFooter()),
                this.options.showTooltip && this.ui.appendChild(this.renderTooltip()),
                (this.ui.dataset.plugins = (this.options.plugins || []).join('|')),
                this.emit('render', this.ui);
            }),
            (e.prototype.renderMonth = function (t, e) {
              var n = this,
                i = t.clone(),
                o = 32 - new Date(i.getFullYear(), i.getMonth(), 32).getDate(),
                s = document.createElement('div');
              s.className = a.monthItem;
              var l = document.createElement('div');
              l.className = a.monthItemHeader;
              var p = document.createElement('div');
              if (this.options.dropdowns.months) {
                var c = document.createElement('select');
                c.className = a.monthItemName;
                for (var d = 0; d < 12; d += 1) {
                  var u = document.createElement('option'),
                    m = new r.DateTime(new Date(t.getFullYear(), d, 2, 0, 0, 0)),
                    f = new r.DateTime(new Date(t.getFullYear(), d, 1, 0, 0, 0));
                  (u.value = String(d)),
                    (u.text = m.toLocaleString(this.options.lang, { month: 'long' })),
                    (u.disabled =
                      (this.options.minDate && f.isBefore(new r.DateTime(this.options.minDate), 'month')) ||
                      (this.options.maxDate && f.isAfter(new r.DateTime(this.options.maxDate), 'month'))),
                    (u.selected = f.getMonth() === t.getMonth()),
                    c.appendChild(u);
                }
                c.addEventListener('change', function (t) {
                  var e = t.target,
                    i = 0;
                  if (n.options.splitView) {
                    var o = e.closest('.' + a.monthItem);
                    i = h.findNestedMonthItem(o);
                  }
                  n.calendars[i].setMonth(Number(e.value)), n.render(), n.emit('change:month', n.calendars[i], i, t);
                }),
                  p.appendChild(c);
              } else {
                ((m = document.createElement('strong')).className = a.monthItemName),
                  (m.innerHTML = t.toLocaleString(this.options.lang, { month: 'long' })),
                  p.appendChild(m);
              }
              if (this.options.dropdowns.years) {
                var g = document.createElement('select');
                g.className = a.monthItemYear;
                var y = this.options.dropdowns.minYear,
                  v = this.options.dropdowns.maxYear ? this.options.dropdowns.maxYear : new Date().getFullYear();
                if (t.getFullYear() > v)
                  ((u = document.createElement('option')).value = String(t.getFullYear())),
                    (u.text = String(t.getFullYear())),
                    (u.selected = !0),
                    (u.disabled = !0),
                    g.appendChild(u);
                for (d = v; d >= y; d -= 1) {
                  var u = document.createElement('option'),
                    D = new r.DateTime(new Date(d, 0, 1, 0, 0, 0));
                  (u.value = String(d)),
                    (u.text = String(d)),
                    (u.disabled =
                      (this.options.minDate && D.isBefore(new r.DateTime(this.options.minDate), 'year')) ||
                      (this.options.maxDate && D.isAfter(new r.DateTime(this.options.maxDate), 'year'))),
                    (u.selected = t.getFullYear() === d),
                    g.appendChild(u);
                }
                if (t.getFullYear() < y)
                  ((u = document.createElement('option')).value = String(t.getFullYear())),
                    (u.text = String(t.getFullYear())),
                    (u.selected = !0),
                    (u.disabled = !0),
                    g.appendChild(u);
                if ('asc' === this.options.dropdowns.years) {
                  var w = Array.prototype.slice.call(g.childNodes).reverse();
                  (g.innerHTML = ''),
                    w.forEach(function (t) {
                      (t.innerHTML = t.value), g.appendChild(t);
                    });
                }
                g.addEventListener('change', function (t) {
                  var e = t.target,
                    i = 0;
                  if (n.options.splitView) {
                    var o = e.closest('.' + a.monthItem);
                    i = h.findNestedMonthItem(o);
                  }
                  n.calendars[i].setFullYear(Number(e.value)), n.render(), n.emit('change:year', n.calendars[i], i, t);
                }),
                  p.appendChild(g);
              } else {
                var M = document.createElement('span');
                (M.className = a.monthItemYear), (M.innerHTML = String(t.getFullYear())), p.appendChild(M);
              }
              var T = document.createElement('button');
              (T.type = 'button'), (T.className = a.buttonPreviousMonth), (T.innerHTML = this.options.buttonText.previousMonth);
              var k = document.createElement('button');
              (k.type = 'button'),
                (k.className = a.buttonNextMonth),
                (k.innerHTML = this.options.buttonText.nextMonth),
                l.appendChild(T),
                l.appendChild(p),
                l.appendChild(k),
                this.options.minDate &&
                  i.isSameOrBefore(new r.DateTime(this.options.minDate), 'month') &&
                  s.classList.add(a.noPreviousMonth),
                this.options.maxDate && i.isSameOrAfter(new r.DateTime(this.options.maxDate), 'month') && s.classList.add(a.noNextMonth);
              var b = document.createElement('div');
              (b.className = a.monthItemWeekdaysRow), this.options.showWeekNumbers && (b.innerHTML = '<div>W</div>');
              for (var L = 1; L <= 7; L += 1) {
                var E = 3 + this.options.firstDay + L,
                  S = document.createElement('div');
                (S.innerHTML = this.weekdayName(E)), (S.title = this.weekdayName(E, 'long')), b.appendChild(S);
              }
              var P = document.createElement('div');
              P.className = a.containerDays;
              var x = this.calcSkipDays(i);
              this.options.showWeekNumbers && x && P.appendChild(this.renderWeekNumber(i));
              for (var I = 0; I < x; I += 1) {
                var _ = document.createElement('div');
                P.appendChild(_);
              }
              for (I = 1; I <= o; I += 1)
                i.setDate(I),
                  this.options.showWeekNumbers && i.getDay() === this.options.firstDay && P.appendChild(this.renderWeekNumber(i)),
                  P.appendChild(this.renderDay(i));
              return s.appendChild(l), s.appendChild(b), s.appendChild(P), this.emit('render:month', s, t), s;
            }),
            (e.prototype.renderDay = function (t) {
              t.setHours();
              var e = document.createElement('div');
              if (
                ((e.className = a.dayItem),
                (e.innerHTML = String(t.getDate())),
                (e.dataset.time = String(t.getTime())),
                t.toDateString() === new Date().toDateString() && e.classList.add(a.isToday),
                this.datePicked.length)
              )
                this.datePicked[0].toDateString() === t.toDateString() &&
                  (e.classList.add(a.isStartDate), this.options.singleMode && e.classList.add(a.isEndDate)),
                  2 === this.datePicked.length && this.datePicked[1].toDateString() === t.toDateString() && e.classList.add(a.isEndDate),
                  2 === this.datePicked.length && t.isBetween(this.datePicked[0], this.datePicked[1]) && e.classList.add(a.isInRange);
              else if (this.options.startDate) {
                var n = this.options.startDate,
                  i = this.options.endDate;
                n.toDateString() === t.toDateString() &&
                  (e.classList.add(a.isStartDate), this.options.singleMode && e.classList.add(a.isEndDate)),
                  i && i.toDateString() === t.toDateString() && e.classList.add(a.isEndDate),
                  n && i && t.isBetween(n, i) && e.classList.add(a.isInRange);
              }
              if (
                (this.options.minDate && t.isBefore(new r.DateTime(this.options.minDate)) && e.classList.add(a.isLocked),
                this.options.maxDate && t.isAfter(new r.DateTime(this.options.maxDate)) && e.classList.add(a.isLocked),
                this.options.minDays > 1 && 1 === this.datePicked.length)
              ) {
                var o = this.options.minDays - 1,
                  s = this.datePicked[0].clone().subtract(o, 'day'),
                  l = this.datePicked[0].clone().add(o, 'day');
                t.isBetween(s, this.datePicked[0], '(]') && e.classList.add(a.isLocked),
                  t.isBetween(this.datePicked[0], l, '[)') && e.classList.add(a.isLocked);
              }
              if (this.options.maxDays && 1 === this.datePicked.length) {
                var p = this.options.maxDays;
                (s = this.datePicked[0].clone().subtract(p, 'day')), (l = this.datePicked[0].clone().add(p, 'day'));
                t.isSameOrBefore(s) && e.classList.add(a.isLocked), t.isSameOrAfter(l) && e.classList.add(a.isLocked);
              }
              (this.options.selectForward && 1 === this.datePicked.length && t.isBefore(this.datePicked[0]) && e.classList.add(a.isLocked),
              this.options.selectBackward && 1 === this.datePicked.length && t.isAfter(this.datePicked[0]) && e.classList.add(a.isLocked),
              h.dateIsLocked(t, this.options, this.datePicked) && e.classList.add(a.isLocked),
              this.options.highlightedDays.length) &&
                this.options.highlightedDays.filter(function (e) {
                  return e instanceof Array ? t.isBetween(e[0], e[1], '[]') : e.isSame(t, 'day');
                }).length &&
                e.classList.add(a.isHighlighted);
              return (e.tabIndex = e.classList.contains('is-locked') ? -1 : 0), this.emit('render:day', e, t), e;
            }),
            (e.prototype.renderFooter = function () {
              var t = document.createElement('div');
              if (
                ((t.className = a.containerFooter),
                this.options.footerHTML
                  ? (t.innerHTML = this.options.footerHTML)
                  : (t.innerHTML =
                      '\n      <span class="' +
                      a.previewDateRange +
                      '"></span>\n      <button type="button" class="' +
                      a.buttonCancel +
                      '">' +
                      this.options.buttonText.cancel +
                      '</button>\n      <button type="button" class="' +
                      a.buttonApply +
                      '">' +
                      this.options.buttonText.apply +
                      '</button>\n      '),
                this.options.singleMode)
              ) {
                if (1 === this.datePicked.length) {
                  var e = this.datePicked[0].format(this.options.format, this.options.lang);
                  t.querySelector('.' + a.previewDateRange).innerHTML = e;
                }
              } else if (
                (1 === this.datePicked.length && t.querySelector('.' + a.buttonApply).setAttribute('disabled', ''),
                2 === this.datePicked.length)
              ) {
                e = this.datePicked[0].format(this.options.format, this.options.lang);
                var n = this.datePicked[1].format(this.options.format, this.options.lang);
                t.querySelector('.' + a.previewDateRange).innerHTML = '' + e + this.options.delimiter + n;
              }
              return this.emit('render:footer', t), t;
            }),
            (e.prototype.renderWeekNumber = function (t) {
              var e = document.createElement('div'),
                n = t.getWeek(this.options.firstDay);
              return (e.className = a.weekNumber), (e.innerHTML = 53 === n && 0 === t.getMonth() ? '53 / 1' : n), e;
            }),
            (e.prototype.renderTooltip = function () {
              var t = document.createElement('div');
              return (t.className = a.containerTooltip), t;
            }),
            (e.prototype.weekdayName = function (t, e) {
              return void 0 === e && (e = 'short'), new Date(1970, 0, t, 12, 0, 0, 0).toLocaleString(this.options.lang, { weekday: e });
            }),
            (e.prototype.calcSkipDays = function (t) {
              var e = t.getDay() - this.options.firstDay;
              return e < 0 && (e += 7), e;
            }),
            e
          );
        })(s.LPCore);
      e.Calendar = l;
    },
    function (t, e, n) {
      'use strict';
      var i,
        o =
          (this && this.__extends) ||
          ((i = function (t, e) {
            return (i =
              Object.setPrototypeOf ||
              ({ __proto__: [] } instanceof Array &&
                function (t, e) {
                  t.__proto__ = e;
                }) ||
              function (t, e) {
                for (var n in e) e.hasOwnProperty(n) && (t[n] = e[n]);
              })(t, e);
          }),
          function (t, e) {
            function n() {
              this.constructor = t;
            }
            i(t, e), (t.prototype = null === e ? Object.create(e) : ((n.prototype = e.prototype), new n()));
          }),
        s =
          (this && this.__assign) ||
          function () {
            return (s =
              Object.assign ||
              function (t) {
                for (var e, n = 1, i = arguments.length; n < i; n++)
                  for (var o in (e = arguments[n])) Object.prototype.hasOwnProperty.call(e, o) && (t[o] = e[o]);
                return t;
              }).apply(this, arguments);
          };
      Object.defineProperty(e, '__esModule', { value: !0 });
      var r = n(7),
        a = n(0),
        h = n(1),
        l = (function (t) {
          function e(e) {
            var n = t.call(this) || this;
            (n.datePicked = []),
              (n.calendars = []),
              (n.options = {
                element: null,
                elementEnd: null,
                parentEl: null,
                firstDay: 1,
                format: 'YYYY-MM-DD',
                lang: 'en-US',
                delimiter: ' - ',
                numberOfMonths: 1,
                numberOfColumns: 1,
                startDate: null,
                endDate: null,
                zIndex: 9999,
                position: 'auto',
                selectForward: !1,
                selectBackward: !1,
                splitView: !1,
                inlineMode: !1,
                singleMode: !0,
                autoApply: !0,
                allowRepick: !1,
                showWeekNumbers: !1,
                showTooltip: !0,
                scrollToDate: !0,
                mobileFriendly: !0,
                resetButton: !1,
                autoRefresh: !1,
                lockDaysFormat: 'YYYY-MM-DD',
                lockDays: [],
                disallowLockDaysInRange: !1,
                lockDaysInclusivity: '[]',
                highlightedDaysFormat: 'YYYY-MM-DD',
                highlightedDays: [],
                dropdowns: { minYear: 1990, maxYear: null, months: !1, years: !1 },
                buttonText: {
                  apply: 'Apply',
                  cancel: 'Cancel',
                  previousMonth:
                    '<svg width="11" height="16" xmlns="http://www.w3.org/2000/svg"><path d="M7.919 0l2.748 2.667L5.333 8l5.334 5.333L7.919 16 0 8z" fill-rule="nonzero"/></svg>',
                  nextMonth:
                    '<svg width="11" height="16" xmlns="http://www.w3.org/2000/svg"><path d="M2.748 16L0 13.333 5.333 8 0 2.667 2.748 0l7.919 8z" fill-rule="nonzero"/></svg>',
                  reset:
                    '<svg xmlns="http://www.w3.org/2000/svg" height="24" viewBox="0 0 24 24" width="24">\n        <path d="M0 0h24v24H0z" fill="none"/>\n        <path d="M13 3c-4.97 0-9 4.03-9 9H1l3.89 3.89.07.14L9 12H6c0-3.87 3.13-7 7-7s7 3.13 7 7-3.13 7-7 7c-1.93 0-3.68-.79-4.94-2.06l-1.42 1.42C8.27 19.99 10.51 21 13 21c4.97 0 9-4.03 9-9s-4.03-9-9-9zm-1 5v5l4.28 2.54.72-1.21-3.5-2.08V8H12z"/>\n      </svg>',
                },
                tooltipText: { one: 'day', other: 'days' },
              }),
              (n.options = s(s({}, n.options), e.element.dataset)),
              Object.keys(n.options).forEach(function (t) {
                ('true' !== n.options[t] && 'false' !== n.options[t]) || (n.options[t] = 'true' === n.options[t]);
              });
            var i = s(s({}, n.options.dropdowns), e.dropdowns),
              o = s(s({}, n.options.buttonText), e.buttonText),
              r = s(s({}, n.options.tooltipText), e.tooltipText);
            (n.options = s(s({}, n.options), e)),
              (n.options.dropdowns = s({}, i)),
              (n.options.buttonText = s({}, o)),
              (n.options.tooltipText = s({}, r)),
              n.options.elementEnd || (n.options.allowRepick = !1),
              n.options.lockDays.length && (n.options.lockDays = a.DateTime.convertArray(n.options.lockDays, n.options.lockDaysFormat)),
              n.options.highlightedDays.length &&
                (n.options.highlightedDays = a.DateTime.convertArray(n.options.highlightedDays, n.options.highlightedDaysFormat));
            var h = n.parseInput(),
              l = h[0],
              p = h[1];
            n.options.startDate &&
              (n.options.singleMode || n.options.endDate) &&
              (l = new a.DateTime(n.options.startDate, n.options.format, n.options.lang)),
              l && n.options.endDate && (p = new a.DateTime(n.options.endDate, n.options.format, n.options.lang)),
              l instanceof a.DateTime && !isNaN(l.getTime()) && (n.options.startDate = l),
              n.options.startDate && p instanceof a.DateTime && !isNaN(p.getTime()) && (n.options.endDate = p),
              !n.options.singleMode || n.options.startDate instanceof a.DateTime || (n.options.startDate = null),
              n.options.singleMode ||
                (n.options.startDate instanceof a.DateTime && n.options.endDate instanceof a.DateTime) ||
                ((n.options.startDate = null), (n.options.endDate = null));
            for (var c = 0; c < n.options.numberOfMonths; c += 1) {
              var d = n.options.startDate instanceof a.DateTime ? n.options.startDate.clone() : new a.DateTime();
              if (!n.options.startDate && (0 === c || n.options.splitView)) {
                var u = n.options.maxDate ? new a.DateTime(n.options.maxDate) : null,
                  m = n.options.minDate ? new a.DateTime(n.options.minDate) : null,
                  f = n.options.numberOfMonths - 1;
                m && u && d.isAfter(u)
                  ? (d = m.clone()).setDate(1)
                  : !m && u && d.isAfter(u) && ((d = u.clone()).setDate(1), d.setMonth(d.getMonth() - f));
              }
              d.setDate(1), d.setMonth(d.getMonth() + c), (n.calendars[c] = d);
            }
            if (n.options.showTooltip)
              if (n.options.tooltipPluralSelector) n.pluralSelector = n.options.tooltipPluralSelector;
              else
                try {
                  var g = new Intl.PluralRules(n.options.lang);
                  n.pluralSelector = g.select.bind(g);
                } catch (t) {
                  n.pluralSelector = function (t) {
                    return 0 === Math.abs(t) ? 'one' : 'other';
                  };
                }
            return n;
          }
          return (
            o(e, t),
            (e.add = function (t, e) {
              h.Litepicker.prototype[t] = e;
            }),
            (e.prototype.DateTime = function (t, e) {
              return t ? new a.DateTime(t, e) : new a.DateTime();
            }),
            (e.prototype.init = function () {
              var t = this;
              this.options.plugins &&
                this.options.plugins.length &&
                this.options.plugins.forEach(function (e) {
                  h.Litepicker.prototype.hasOwnProperty(e)
                    ? h.Litepicker.prototype[e].init.call(t, t)
                    : console.warn('Litepicker: plugin «' + e + '» not found.');
                });
            }),
            (e.prototype.parseInput = function () {
              var t = this.options.delimiter,
                e = new RegExp('' + t),
                n = this.options.element instanceof HTMLInputElement ? this.options.element.value.split(t) : [];
              if (this.options.elementEnd) {
                if (
                  this.options.element instanceof HTMLInputElement &&
                  this.options.element.value.length &&
                  this.options.elementEnd instanceof HTMLInputElement &&
                  this.options.elementEnd.value.length
                )
                  return [
                    new a.DateTime(this.options.element.value, this.options.format),
                    new a.DateTime(this.options.elementEnd.value, this.options.format),
                  ];
              } else if (this.options.singleMode) {
                if (this.options.element instanceof HTMLInputElement && this.options.element.value.length)
                  return [new a.DateTime(this.options.element.value, this.options.format)];
              } else if (
                this.options.element instanceof HTMLInputElement &&
                e.test(this.options.element.value) &&
                n.length &&
                n.length % 2 == 0
              ) {
                var i = n.slice(0, n.length / 2).join(t),
                  o = n.slice(n.length / 2).join(t);
                return [new a.DateTime(i, this.options.format), new a.DateTime(o, this.options.format)];
              }
              return [];
            }),
            (e.prototype.isShowning = function () {
              return this.ui && 'none' !== this.ui.style.display;
            }),
            (e.prototype.findPosition = function (t) {
              var e = t.getBoundingClientRect(),
                n = this.ui.getBoundingClientRect(),
                i = this.options.position.split(' '),
                o = window.scrollX || window.pageXOffset,
                s = window.scrollY || window.pageYOffset,
                r = 0,
                a = 0;
              if ('auto' !== i[0] && /top|bottom/.test(i[0])) (r = e[i[0]] + s), 'top' === i[0] && (r -= n.height);
              else {
                r = e.bottom + s;
                var h = e.bottom + n.height > window.innerHeight,
                  l = e.top + s - n.height >= n.height;
                h && l && (r = e.top + s - n.height);
              }
              if (/left|right/.test(i[0]) || (i[1] && 'auto' !== i[1] && /left|right/.test(i[1])))
                (a = /left|right/.test(i[0]) ? e[i[0]] + o : e[i[1]] + o), ('right' !== i[0] && 'right' !== i[1]) || (a -= n.width);
              else {
                a = e.left + o;
                h = e.left + n.width > window.innerWidth;
                var p = e.right + o - n.width >= 0;
                h && p && (a = e.right + o - n.width);
              }
              return { left: a, top: r };
            }),
            e
          );
        })(r.EventEmitter);
      e.LPCore = l;
    },
    function (t, e, n) {
      'use strict';
      var i,
        o = 'object' == typeof Reflect ? Reflect : null,
        s =
          o && 'function' == typeof o.apply
            ? o.apply
            : function (t, e, n) {
                return Function.prototype.apply.call(t, e, n);
              };
      i =
        o && 'function' == typeof o.ownKeys
          ? o.ownKeys
          : Object.getOwnPropertySymbols
          ? function (t) {
              return Object.getOwnPropertyNames(t).concat(Object.getOwnPropertySymbols(t));
            }
          : function (t) {
              return Object.getOwnPropertyNames(t);
            };
      var r =
        Number.isNaN ||
        function (t) {
          return t != t;
        };
      function a() {
        a.init.call(this);
      }
      (t.exports = a),
        (a.EventEmitter = a),
        (a.prototype._events = void 0),
        (a.prototype._eventsCount = 0),
        (a.prototype._maxListeners = void 0);
      var h = 10;
      function l(t) {
        return void 0 === t._maxListeners ? a.defaultMaxListeners : t._maxListeners;
      }
      function p(t, e, n, i) {
        var o, s, r, a;
        if ('function' != typeof n) throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof n);
        if (
          (void 0 === (s = t._events)
            ? ((s = t._events = Object.create(null)), (t._eventsCount = 0))
            : (void 0 !== s.newListener && (t.emit('newListener', e, n.listener ? n.listener : n), (s = t._events)), (r = s[e])),
          void 0 === r)
        )
          (r = s[e] = n), ++t._eventsCount;
        else if (
          ('function' == typeof r ? (r = s[e] = i ? [n, r] : [r, n]) : i ? r.unshift(n) : r.push(n),
          (o = l(t)) > 0 && r.length > o && !r.warned)
        ) {
          r.warned = !0;
          var h = new Error(
            'Possible EventEmitter memory leak detected. ' +
              r.length +
              ' ' +
              String(e) +
              ' listeners added. Use emitter.setMaxListeners() to increase limit'
          );
          (h.name = 'MaxListenersExceededWarning'),
            (h.emitter = t),
            (h.type = e),
            (h.count = r.length),
            (a = h),
            console && console.warn && console.warn(a);
        }
        return t;
      }
      function c() {
        for (var t = [], e = 0; e < arguments.length; e++) t.push(arguments[e]);
        this.fired || (this.target.removeListener(this.type, this.wrapFn), (this.fired = !0), s(this.listener, this.target, t));
      }
      function d(t, e, n) {
        var i = { fired: !1, wrapFn: void 0, target: t, type: e, listener: n },
          o = c.bind(i);
        return (o.listener = n), (i.wrapFn = o), o;
      }
      function u(t, e, n) {
        var i = t._events;
        if (void 0 === i) return [];
        var o = i[e];
        return void 0 === o
          ? []
          : 'function' == typeof o
          ? n
            ? [o.listener || o]
            : [o]
          : n
          ? (function (t) {
              for (var e = new Array(t.length), n = 0; n < e.length; ++n) e[n] = t[n].listener || t[n];
              return e;
            })(o)
          : f(o, o.length);
      }
      function m(t) {
        var e = this._events;
        if (void 0 !== e) {
          var n = e[t];
          if ('function' == typeof n) return 1;
          if (void 0 !== n) return n.length;
        }
        return 0;
      }
      function f(t, e) {
        for (var n = new Array(e), i = 0; i < e; ++i) n[i] = t[i];
        return n;
      }
      Object.defineProperty(a, 'defaultMaxListeners', {
        enumerable: !0,
        get: function () {
          return h;
        },
        set: function (t) {
          if ('number' != typeof t || t < 0 || r(t))
            throw new RangeError(
              'The value of "defaultMaxListeners" is out of range. It must be a non-negative number. Received ' + t + '.'
            );
          h = t;
        },
      }),
        (a.init = function () {
          (void 0 !== this._events && this._events !== Object.getPrototypeOf(this)._events) ||
            ((this._events = Object.create(null)), (this._eventsCount = 0)),
            (this._maxListeners = this._maxListeners || void 0);
        }),
        (a.prototype.setMaxListeners = function (t) {
          if ('number' != typeof t || t < 0 || r(t))
            throw new RangeError('The value of "n" is out of range. It must be a non-negative number. Received ' + t + '.');
          return (this._maxListeners = t), this;
        }),
        (a.prototype.getMaxListeners = function () {
          return l(this);
        }),
        (a.prototype.emit = function (t) {
          for (var e = [], n = 1; n < arguments.length; n++) e.push(arguments[n]);
          var i = 'error' === t,
            o = this._events;
          if (void 0 !== o) i = i && void 0 === o.error;
          else if (!i) return !1;
          if (i) {
            var r;
            if ((e.length > 0 && (r = e[0]), r instanceof Error)) throw r;
            var a = new Error('Unhandled error.' + (r ? ' (' + r.message + ')' : ''));
            throw ((a.context = r), a);
          }
          var h = o[t];
          if (void 0 === h) return !1;
          if ('function' == typeof h) s(h, this, e);
          else {
            var l = h.length,
              p = f(h, l);
            for (n = 0; n < l; ++n) s(p[n], this, e);
          }
          return !0;
        }),
        (a.prototype.addListener = function (t, e) {
          return p(this, t, e, !1);
        }),
        (a.prototype.on = a.prototype.addListener),
        (a.prototype.prependListener = function (t, e) {
          return p(this, t, e, !0);
        }),
        (a.prototype.once = function (t, e) {
          if ('function' != typeof e) throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof e);
          return this.on(t, d(this, t, e)), this;
        }),
        (a.prototype.prependOnceListener = function (t, e) {
          if ('function' != typeof e) throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof e);
          return this.prependListener(t, d(this, t, e)), this;
        }),
        (a.prototype.removeListener = function (t, e) {
          var n, i, o, s, r;
          if ('function' != typeof e) throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof e);
          if (void 0 === (i = this._events)) return this;
          if (void 0 === (n = i[t])) return this;
          if (n === e || n.listener === e)
            0 == --this._eventsCount
              ? (this._events = Object.create(null))
              : (delete i[t], i.removeListener && this.emit('removeListener', t, n.listener || e));
          else if ('function' != typeof n) {
            for (o = -1, s = n.length - 1; s >= 0; s--)
              if (n[s] === e || n[s].listener === e) {
                (r = n[s].listener), (o = s);
                break;
              }
            if (o < 0) return this;
            0 === o
              ? n.shift()
              : (function (t, e) {
                  for (; e + 1 < t.length; e++) t[e] = t[e + 1];
                  t.pop();
                })(n, o),
              1 === n.length && (i[t] = n[0]),
              void 0 !== i.removeListener && this.emit('removeListener', t, r || e);
          }
          return this;
        }),
        (a.prototype.off = a.prototype.removeListener),
        (a.prototype.removeAllListeners = function (t) {
          var e, n, i;
          if (void 0 === (n = this._events)) return this;
          if (void 0 === n.removeListener)
            return (
              0 === arguments.length
                ? ((this._events = Object.create(null)), (this._eventsCount = 0))
                : void 0 !== n[t] && (0 == --this._eventsCount ? (this._events = Object.create(null)) : delete n[t]),
              this
            );
          if (0 === arguments.length) {
            var o,
              s = Object.keys(n);
            for (i = 0; i < s.length; ++i) 'removeListener' !== (o = s[i]) && this.removeAllListeners(o);
            return this.removeAllListeners('removeListener'), (this._events = Object.create(null)), (this._eventsCount = 0), this;
          }
          if ('function' == typeof (e = n[t])) this.removeListener(t, e);
          else if (void 0 !== e) for (i = e.length - 1; i >= 0; i--) this.removeListener(t, e[i]);
          return this;
        }),
        (a.prototype.listeners = function (t) {
          return u(this, t, !0);
        }),
        (a.prototype.rawListeners = function (t) {
          return u(this, t, !1);
        }),
        (a.listenerCount = function (t, e) {
          return 'function' == typeof t.listenerCount ? t.listenerCount(e) : m.call(t, e);
        }),
        (a.prototype.listenerCount = m),
        (a.prototype.eventNames = function () {
          return this._eventsCount > 0 ? i(this._events) : [];
        });
    },
    function (t, e, n) {
      'use strict';
      var i =
        (this && this.__assign) ||
        function () {
          return (i =
            Object.assign ||
            function (t) {
              for (var e, n = 1, i = arguments.length; n < i; n++)
                for (var o in (e = arguments[n])) Object.prototype.hasOwnProperty.call(e, o) && (t[o] = e[o]);
              return t;
            }).apply(this, arguments);
        };
      Object.defineProperty(e, '__esModule', { value: !0 });
      var o = n(0),
        s = n(1),
        r = n(2);
      (s.Litepicker.prototype.show = function (t) {
        void 0 === t && (t = null), this.emit('before:show', t);
        var e = t || this.options.element;
        if (((this.triggerElement = e), !this.isShowning())) {
          if (this.options.inlineMode)
            return (
              (this.ui.style.position = 'relative'),
              (this.ui.style.display = 'inline-block'),
              (this.ui.style.top = null),
              (this.ui.style.left = null),
              (this.ui.style.bottom = null),
              void (this.ui.style.right = null)
            );
          this.scrollToDate(t),
            this.render(),
            (this.ui.style.position = 'absolute'),
            (this.ui.style.display = 'block'),
            (this.ui.style.zIndex = this.options.zIndex);
          var n = this.findPosition(e);
          (this.ui.style.top = n.top + 'px'),
            (this.ui.style.left = n.left + 'px'),
            (this.ui.style.right = null),
            (this.ui.style.bottom = null),
            this.emit('show', t);
        }
      }),
        (s.Litepicker.prototype.hide = function () {
          this.isShowning() &&
            ((this.datePicked.length = 0),
            this.updateInput(),
            this.options.inlineMode ? this.render() : ((this.ui.style.display = 'none'), this.emit('hide')));
        }),
        (s.Litepicker.prototype.getDate = function () {
          return this.getStartDate();
        }),
        (s.Litepicker.prototype.getStartDate = function () {
          return this.options.startDate ? this.options.startDate.clone() : null;
        }),
        (s.Litepicker.prototype.getEndDate = function () {
          return this.options.endDate ? this.options.endDate.clone() : null;
        }),
        (s.Litepicker.prototype.setDate = function (t, e) {
          void 0 === e && (e = !1);
          var n = new o.DateTime(t, this.options.format, this.options.lang);
          r.dateIsLocked(n, this.options, [n]) && !e
            ? this.emit('error:date', n)
            : (this.setStartDate(t), this.options.inlineMode && this.render(), this.emit('selected', this.getDate()));
        }),
        (s.Litepicker.prototype.setStartDate = function (t) {
          t && ((this.options.startDate = new o.DateTime(t, this.options.format, this.options.lang)), this.updateInput());
        }),
        (s.Litepicker.prototype.setEndDate = function (t) {
          t &&
            ((this.options.endDate = new o.DateTime(t, this.options.format, this.options.lang)),
            this.options.startDate.getTime() > this.options.endDate.getTime() &&
              ((this.options.endDate = this.options.startDate.clone()),
              (this.options.startDate = new o.DateTime(t, this.options.format, this.options.lang))),
            this.updateInput());
        }),
        (s.Litepicker.prototype.setDateRange = function (t, e, n) {
          void 0 === n && (n = !1), (this.triggerElement = void 0);
          var i = new o.DateTime(t, this.options.format, this.options.lang),
            s = new o.DateTime(e, this.options.format, this.options.lang);
          (this.options.disallowLockDaysInRange
            ? r.rangeIsLocked([i, s], this.options)
            : r.dateIsLocked(i, this.options, [i, s]) || r.dateIsLocked(s, this.options, [i, s])) && !n
            ? this.emit('error:range', [i, s])
            : (this.setStartDate(i),
              this.setEndDate(s),
              this.options.inlineMode && this.render(),
              this.updateInput(),
              this.emit('selected', this.getStartDate(), this.getEndDate()));
        }),
        (s.Litepicker.prototype.gotoDate = function (t, e) {
          void 0 === e && (e = 0);
          var n = new o.DateTime(t);
          n.setDate(1), (this.calendars[e] = n.clone()), this.render();
        }),
        (s.Litepicker.prototype.setLockDays = function (t) {
          (this.options.lockDays = o.DateTime.convertArray(t, this.options.lockDaysFormat)), this.render();
        }),
        (s.Litepicker.prototype.setHighlightedDays = function (t) {
          (this.options.highlightedDays = o.DateTime.convertArray(t, this.options.highlightedDaysFormat)), this.render();
        }),
        (s.Litepicker.prototype.setOptions = function (t) {
          delete t.element,
            delete t.elementEnd,
            delete t.parentEl,
            t.startDate && (t.startDate = new o.DateTime(t.startDate, this.options.format, this.options.lang)),
            t.endDate && (t.endDate = new o.DateTime(t.endDate, this.options.format, this.options.lang));
          var e = i(i({}, this.options.dropdowns), t.dropdowns),
            n = i(i({}, this.options.buttonText), t.buttonText),
            s = i(i({}, this.options.tooltipText), t.tooltipText);
          (this.options = i(i({}, this.options), t)),
            (this.options.dropdowns = i({}, e)),
            (this.options.buttonText = i({}, n)),
            (this.options.tooltipText = i({}, s)),
            !this.options.singleMode ||
              this.options.startDate instanceof o.DateTime ||
              ((this.options.startDate = null), (this.options.endDate = null)),
            this.options.singleMode ||
              (this.options.startDate instanceof o.DateTime && this.options.endDate instanceof o.DateTime) ||
              ((this.options.startDate = null), (this.options.endDate = null));
          for (var r = 0; r < this.options.numberOfMonths; r += 1) {
            var a = this.options.startDate ? this.options.startDate.clone() : new o.DateTime();
            a.setDate(1), a.setMonth(a.getMonth() + r), (this.calendars[r] = a);
          }
          this.options.lockDays.length &&
            (this.options.lockDays = o.DateTime.convertArray(this.options.lockDays, this.options.lockDaysFormat)),
            this.options.highlightedDays.length &&
              (this.options.highlightedDays = o.DateTime.convertArray(this.options.highlightedDays, this.options.highlightedDaysFormat)),
            this.render(),
            this.options.inlineMode && this.show(),
            this.updateInput();
        }),
        (s.Litepicker.prototype.clearSelection = function () {
          (this.options.startDate = null),
            (this.options.endDate = null),
            (this.datePicked.length = 0),
            this.updateInput(),
            this.isShowning() && this.render(),
            this.emit('clear:selection');
        }),
        (s.Litepicker.prototype.destroy = function () {
          this.ui && this.ui.parentNode && (this.ui.parentNode.removeChild(this.ui), (this.ui = null)), this.emit('destroy');
        });
    },
  ]);
});
